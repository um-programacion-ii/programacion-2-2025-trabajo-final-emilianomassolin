package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.service.SaleService;
import com.mycompany.myapp.service.SeatSelectionService;
import com.mycompany.myapp.service.dto.catedra.VentaRequestCatedraDTO;
import com.mycompany.myapp.service.dto.mobile.VentaMobileRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.mycompany.myapp.service.dto.mobile.SeatSelectionMobileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/api/mobile")
public class MobileSaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(MobileSaleResource.class);

    private final SaleService saleService;
    private final SeatSelectionService seatSelectionService;

    public MobileSaleResource(SaleService saleService, SeatSelectionService seatSelectionService) {
        this.saleService = saleService;
        this.seatSelectionService = seatSelectionService;
    }

    @PostMapping("/venta")
    public ResponseEntity<?> realizarVenta(@RequestBody VentaMobileRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: realizar venta. Usuario={}, request={}", username, request);

        // 1) Buscar selección ACTIVA para (evento, usuario)
        Optional<SeatSelection> selectionOpt =
            seatSelectionService.findActiveSelection(request.getEventoId(), username);

        if (selectionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay asientos bloqueados para este evento o la selección expiró");
        }

        SeatSelection selection = selectionOpt.get();

        // 2) Parsear asientos guardados en SeatSelection (string tipo "(1,1),(1,2)")
        List<PosicionAsiento> posiciones = parsearAsientos(selection.getAsientos());

        if (posiciones.isEmpty()) {
            return ResponseEntity.badRequest().body("La selección de asientos está vacía");
        }

        if (request.getPersonas() == null || request.getPersonas().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe enviar la lista de personas");
        }

        if (request.getPersonas().size() != posiciones.size()) {
            return ResponseEntity.badRequest().body("La cantidad de personas no coincide con la cantidad de asientos");
        }

        // 3) Armar VentaRequestCatedraDTO a partir de SeatSelection + personas
        VentaRequestCatedraDTO ventaRequest = new VentaRequestCatedraDTO();
        ventaRequest.setEventoId(request.getEventoId());
        ventaRequest.setFecha(request.getFecha());
        ventaRequest.setPrecioVenta(request.getPrecioVenta());

        List<VentaRequestCatedraDTO.AsientoVentaDTO> asientosVenta = new ArrayList<>();

        for (int i = 0; i < posiciones.size(); i++) {
            PosicionAsiento pos = posiciones.get(i);
            String persona = request.getPersonas().get(i);

            VentaRequestCatedraDTO.AsientoVentaDTO asientoDto = new VentaRequestCatedraDTO.AsientoVentaDTO();
            asientoDto.setFila(pos.getFila());       // int -> Integer (autobox)
            asientoDto.setColumna(pos.getColumna()); // int -> Integer
            asientoDto.setPersona(persona);

            asientosVenta.add(asientoDto);
        }

        ventaRequest.setAsientos(asientosVenta);

        // 4) Ejecutar la venta contra la cátedra (igual que antes)
        Sale sale = saleService.realizarVentaContraCatedra(ventaRequest, username);

// Si la venta fue EXITOSA, eliminamos la selección para no reusarla
        if (Boolean.TRUE.equals(sale.getResultado())) {
            LOG.debug(
                "Venta EXITOSA. Eliminando SeatSelection id={} para usuario={} evento={}",
                selection.getId(),
                username,
                request.getEventoId()
            );
            seatSelectionService.delete(selection.getId());
        }

        return ResponseEntity.ok(sale);

    }

    /**
     * Parsea un string tipo "(1,1),(1,2)" a lista de posiciones.
     * Ajustá esto si tu formato de asientos es distinto.
     */
    private List<PosicionAsiento> parsearAsientos(String asientosString) {
        List<PosicionAsiento> result = new ArrayList<>();
        if (asientosString == null || asientosString.isBlank()) {
            return result;
        }

        String sinEspacios = asientosString.replace(" ", "");
        String[] bloques = sinEspacios.split("\\),\\("); // separa "(1,1),(1,2)" en ["(1,1", "1,2)"]

        for (String b : bloques) {
            String limpio = b.replace("(", "").replace(")", ""); // "1,1"
            String[] partes = limpio.split(",");
            if (partes.length == 2) {
                try {
                    int fila = Integer.parseInt(partes[0]);
                    int columna = Integer.parseInt(partes[1]);
                    result.add(new PosicionAsiento(fila, columna));
                } catch (NumberFormatException e) {
                    LOG.warn("No se pudo parsear asiento '{}'", b);
                }
            }
        }
        return result;
    }

    private static class PosicionAsiento {
        private final int fila;
        private final int columna;

        public PosicionAsiento(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
        }

        public int getFila() {
            return fila;
        }

        public int getColumna() {
            return columna;
        }
    }
    @GetMapping("/seleccion-actual")
    public ResponseEntity<?> obtenerSeleccionActual(@RequestParam("eventoId") Long eventoId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: obtener seleccion actual. Usuario={}, eventoId={}", username, eventoId);

        var selectionOpt = seatSelectionService.findActiveSelection(eventoId, username);

        if (selectionOpt.isEmpty()) {
            // No hay selección vigente → el mobile sabe que debe empezar de cero
            return ResponseEntity.notFound().build();
        }

        var selection = selectionOpt.get();

        // Reusamos parsearAsientos(String)
        List<PosicionAsiento> posiciones = parsearAsientos(selection.getAsientos());

        SeatSelectionMobileDTO dto = new SeatSelectionMobileDTO();
        dto.setEventoId(eventoId);
        dto.setExpiracion(selection.getExpiracion());

        List<SeatSelectionMobileDTO.AsientoSimpleDTO> asientosDTO = new ArrayList<>();
        for (PosicionAsiento pos : posiciones) {
            asientosDTO.add(new SeatSelectionMobileDTO.AsientoSimpleDTO(pos.getFila(), pos.getColumna()));
        }
        dto.setAsientos(asientosDTO);

        return ResponseEntity.ok(dto);
    }
    @GetMapping("/ventas")
    public ResponseEntity<List<Sale>> listarVentasMobile(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: listar ventas. Usuario={}", username);

        // Por ahora usamos el mismo findAll que el recurso normal.
        // Más adelante podés hacer un findByUserLogin(username) si querés filtrar por usuario.
        Page<Sale> page = saleService.findAll(pageable);

        return ResponseEntity.ok(page.getContent());
    }


    @GetMapping("/ventas/{id}")
    public ResponseEntity<Sale> obtenerVentaMobile(@PathVariable Long id) {
        return saleService.findOne(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/ventas/reintentar-pendientes")
    public ResponseEntity<List<Sale>> reintentarVentasPendientes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: reintentar ventas pendientes. Usuario técnico={}", username);

        List<Sale> actualizadas = saleService.reintentarVentasPendientes(username);
        return ResponseEntity.ok(actualizadas);
    }

}
