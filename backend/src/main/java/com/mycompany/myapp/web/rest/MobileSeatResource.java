package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.ProxySeatService;
import com.mycompany.myapp.service.dto.proxy.SeatMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/mobile")
public class MobileSeatResource {

    private final Logger log = LoggerFactory.getLogger(MobileSeatResource.class);

    private final ProxySeatService proxySeatService;

    public MobileSeatResource(ProxySeatService proxySeatService) {
        this.proxySeatService = proxySeatService;
    }

    /**
     * GET /api/mobile/eventos/{eventId}/asientos
     *
     * Devuelve el mapa de asientos del evento, obtenido a través del proxy.
     */
    @GetMapping("/eventos/{eventId}/asientos")
    public ResponseEntity<SeatMapDTO> getSeatMap(@PathVariable Long eventId) {
        log.debug("REST request to get seat map for event {}", eventId);
        try {
            SeatMapDTO map = proxySeatService.getSeatMapForEvent(eventId);
            if (map == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(map);
        } catch (RestClientException e) {
            // Si el proxy está caído o algo falla, devolvemos 503
            return ResponseEntity.status(503).build();
        }
    }
}
