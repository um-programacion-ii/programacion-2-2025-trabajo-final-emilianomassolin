package com.mycompany.myapp.web.rest.mobile;

import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.mobile.MobileSessionService;
import com.mycompany.myapp.service.mobile.MobileSessionState;
import com.mycompany.myapp.service.mobile.MobileSeatSelection;
import com.mycompany.myapp.service.dto.mobile.AsientoSesionDTO;
import com.mycompany.myapp.service.dto.mobile.MobileSessionStateDTO;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller para estado de la sesión mobile.
 */
@RestController
@RequestMapping("/api/mobile/sesion")
public class MobileSessionResource {

    private final Logger log = LoggerFactory.getLogger(MobileSessionResource.class);

    private final MobileSessionService mobileSessionService;

    public MobileSessionResource(MobileSessionService mobileSessionService) {
        this.mobileSessionService = mobileSessionService;
    }

    @GetMapping("/estado-actual")
    public ResponseEntity<MobileSessionStateDTO> getEstadoActual() {
        String username = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("No hay usuario autenticado"));

        log.debug("REST request to get current mobile session state for user {}", username);

        MobileSessionState state = mobileSessionService.getOrCreateSession(username);

        MobileSessionStateDTO dto = new MobileSessionStateDTO();
        dto.setEventoId(state.getEventoId());
        dto.setEtapa(state.getEtapa().name());
        dto.setExpiracion(state.getExpiresAt());

        dto.setAsientos(
            state.getAsientos()
                .stream()
                .map(this::mapSeat)
                .collect(Collectors.toList())
        );

        return ResponseEntity.ok(dto);
    }

    private AsientoSesionDTO mapSeat(MobileSeatSelection seat) {
        return new AsientoSesionDTO(seat.getFila(), seat.getColumna(), seat.getPersona());
    }

    /**
     * Extra opcional útil: endpoint para limpiar sesión (logout) desde mobile.
     */
    @DeleteMapping
    public ResponseEntity<Void> clearSession() {
        String username = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new RuntimeException("No hay usuario autenticado"));

        log.info("REST request to clear mobile session for user {}", username);
        mobileSessionService.clearSession(username);
        return ResponseEntity.noContent().build();
    }
}
