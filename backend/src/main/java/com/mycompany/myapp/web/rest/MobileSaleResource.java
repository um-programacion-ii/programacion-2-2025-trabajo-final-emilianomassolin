package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.service.SaleService;
import com.mycompany.myapp.service.SeatSelectionService;
import com.mycompany.myapp.service.dto.catedra.VentaRequestCatedraDTO;
import com.mycompany.myapp.service.dto.mobile.SeatSelectionMobileDTO;
import com.mycompany.myapp.service.dto.mobile.VentaMobileRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Recurso REST "mobile" para el flujo de venta:
 * - Bloqueo (en otro endpoint)
 * - Selección actual
 * - Venta usando SeatSelection + personas
 * - Listado de ventas, detalle y reintentos.
 */
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

    // ==========================
    // 1) Venta mobile
    // ==========================
    @PostMapping("/venta")
    public ResponseEntity<?> realizarVenta(@RequestBody VentaMobileRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: realizar venta. Usuario={}, request={}", username, request);

        // 1) Buscar selección ACTIVA para (eventoId de la CÁTEDRA, usuario)
        Optional<SeatSelection> selectionOpt = seatSelectionService.findActiveSelection(request.getEventoId(), username);

        if (selectionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay asientos bloqueados para este evento o la selección expiró.");
        }

        // Usamos orElseThrow solo para evitar Optional.get() (no debería ejecutarse nunca porque arriba validamos)
        SeatSelection selection = selectionOpt.orElseThrow(
            () -> new IllegalStateException("No se encontró SeatSelection activa a pesar de la validación previa")
        );

        // 2) Parsear asientos guardados en SeatSelection (string tipo "(1,3),(1,4)")
        List<PosicionAsiento> posiciones = parsearAsientos(selection.getAsientos());

        if (posiciones.isEmpty()) {
            return ResponseEntity.badRequest().body("La selección de asientos está vacía.");
        }

        if (request.getPersonas() == null || request.getPersonas().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe enviar la lista de personas.");
        }

        if (request.getPersonas().size() != posiciones.size()) {
            return ResponseEntity.badRequest().body("La cantidad de personas no coincide con la cantidad de asientos.");
        }

        // 3) Armar VentaRequestCatedraDTO a partir de SeatSelection + personas
        VentaRequestCatedraDTO ventaRequest = new VentaRequestCatedraDTO();
        // IMPORTANTE: este eventoId es el de la CÁTEDRA (1, 2, ...)
        ventaRequest.setEventoId(request.getEventoId());
        ventaRequest.setFecha(request.getFecha());
        ventaRequest.setPrecioVenta(request.getPrecioVenta());

        List<VentaRequestCatedraDTO.AsientoVentaDTO> asientosVenta = new ArrayList<>();

        for (int i = 0; i < posiciones.size(); i++) {
            PosicionAsiento pos = posiciones.get(i);
            String persona = request.getPersonas().get(i);

            VentaRequestCatedraDTO.AsientoVentaDTO asientoDto = new VentaRequestCatedraDTO.AsientoVentaDTO();
            asientoDto.setFila(pos.getFila());
            asientoDto.setColumna(pos.getColumna());
            asientoDto.setPersona(persona);

            asientosVenta.add(asientoDto);
        }

        ventaRequest.setAsientos(asientosVenta);

        LOG.debug("Enviando venta a cátedra: {}", ventaRequest);

        // 4) Ejecutar la venta contra la cátedra
        Sale sale = saleService.realizarVentaContraCatedra(ventaRequest, username);

        // 5) Si la venta fue EXITOSA, eliminamos la selección para no reusarla
        if (Boolean.TRUE.equals(sale.getResultado())) {
            LOG.debug(
                "Venta EXITOSA. Eliminando SeatSelection id={} para usuario={} evento={}",
                selection.getId(),
                username,
                request.getEventoId()
            );
            seatSelectionService.delete(selection.getId());
        } else {
            LOG.debug(
                "Venta NO exitosa (estado={}). Manteniendo SeatSelection id={} para posible reintento.",
                sale.getEstado(),
                selection.getId()
            );
        }

        // Devolvés el Sale completo (podrías mapear a DTO si querés)
        return ResponseEntity.ok(sale);
    }


    /**
     * Parsea un string tipo "(1,3),(1,4)" a lista de posiciones (fila, columna).
     * Formato esperado: lista de pares entre paréntesis separados por coma.
     */
    private List<PosicionAsiento> parsearAsientos(String asientosString) {
        List<PosicionAsiento> result = new ArrayList<>();
        if (asientosString == null || asientosString.isBlank()) {
            return result;
        }

        // Usamos regex para evitar problemas de comas/espacios
        Pattern pattern = Pattern.compile("\\((\\d+),(\\d+)\\)");
        Matcher matcher = pattern.matcher(asientosString);

        while (matcher.find()) {
            try {
                int fila = Integer.parseInt(matcher.group(1));
                int columna = Integer.parseInt(matcher.group(2));
                result.add(new PosicionAsiento(fila, columna));
            } catch (NumberFormatException e) {
                LOG.warn("No se pudo parsear asiento en '{}'", asientosString, e);
            }
        }

        return result;
    }

    /**
     * Clase interna simple para representar un asiento (fila, columna).
     * Se usa internamente para reconstruir la selección guardada como String.
     */
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

    // ==========================
    // 2) Selección actual
    // ==========================
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

        // De nuevo, orElseThrow solo para evitar Optional.get()
        SeatSelection selection = selectionOpt.orElseThrow(
            () -> new IllegalStateException("No se encontró SeatSelection activa a pesar de la validación previa")
        );

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

    // ==========================
    // 3) Listado y detalle de ventas
    // ==========================
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
        return saleService
            .findOne(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ==========================
    // 4) Reintentar ventas pendientes
    // ==========================
    @PostMapping("/ventas/reintentar-pendientes")
    public ResponseEntity<List<Sale>> reintentarVentasPendientes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: reintentar ventas pendientes. Usuario técnico={}", username);

        List<Sale> actualizadas = saleService.reintentarVentasPendientes(username);
        return ResponseEntity.ok(actualizadas);
    }
}
