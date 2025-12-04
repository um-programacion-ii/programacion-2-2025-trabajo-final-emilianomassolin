package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.service.CatedraService;
import com.mycompany.myapp.service.SeatSelectionService;
import com.mycompany.myapp.service.dto.catedra.BloqueoAsientosRequestDTO;
import com.mycompany.myapp.service.dto.catedra.BloqueoAsientosResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile")
public class MobileSeatSelectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(MobileSeatSelectionResource.class);

    private final CatedraService catedraService;
    private final SeatSelectionService seatSelectionService;

    public MobileSeatSelectionResource(CatedraService catedraService, SeatSelectionService seatSelectionService) {
        this.catedraService = catedraService;
        this.seatSelectionService = seatSelectionService;
    }

    /**
     * POST /api/mobile/bloquear-asientos
     *
     * Body esperado (igual que cátedra):
     * {
     *   "eventoId": 1,
     *   "asientos": [
     *     { "fila": 2, "columna": 1 },
     *     { "fila": 2, "columna": 2 }
     *   ]
     * }
     */
    @PostMapping("/bloquear-asientos")
    public ResponseEntity<?> bloquearAsientos(@RequestBody BloqueoAsientosRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";
        LOG.debug("Mobile request bloquear-asientos. User={}, request={}", username, request);

        // 1) Llamar a la cátedra
        BloqueoAsientosResponseDTO respuesta = catedraService.bloquearAsientos(request);

        if (Boolean.FALSE.equals(respuesta.getResultado())) {
            // No se pudieron bloquear → devolvemos 400 con el detalle de la cátedra
            LOG.warn("Bloqueo de asientos fallido. respuesta={}", respuesta);
            return ResponseEntity.badRequest().body(respuesta);
        }

        // 2) Si se bloquearon OK, guardamos la selección local
        // Guardamos los asientos como string "(fila,columna),(fila,columna)"
        String asientosStr = request.getAsientos().stream()
            .map(a -> "(" + a.getFila() + "," + a.getColumna() + ")")
            .reduce((a, b) -> a + "," + b)
            .orElse("");

        SeatSelection selection = seatSelectionService.createOrUpdateSelection(
            request.getEventoId(),
            username,
            asientosStr
        );

        // 3) Podemos devolver tanto la respuesta de cátedra como la selección local
        // Por simplicidad devolvemos la respuesta de la cátedra
        // (el mobile ya sabe qué asientos pidió y el backend se queda con SeatSelection internamente)
        return ResponseEntity.ok(respuesta);
    }
}
