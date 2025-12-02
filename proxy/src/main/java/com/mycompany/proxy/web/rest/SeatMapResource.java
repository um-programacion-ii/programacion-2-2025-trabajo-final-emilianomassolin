package com.mycompany.proxy.web.rest;

import com.mycompany.proxy.service.SeatMapService;
import com.mycompany.proxy.service.dto.SeatMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proxy")
public class SeatMapResource {

    private static final Logger log = LoggerFactory.getLogger(SeatMapResource.class);

    private final SeatMapService seatMapService;

    public SeatMapResource(SeatMapService seatMapService) {
        this.seatMapService = seatMapService;
    }

    /**
     * GET /api/proxy/eventos/asientos?eventoId=1
     *
     * Devuelve el mapa de asientos de un evento.
     */
    @GetMapping("/eventos/asientos")
    public ResponseEntity<SeatMapDTO> getSeatMap(@RequestParam("eventoId") Long eventoId) {
        log.debug("REST request to get seat map for event {}", eventoId);
        SeatMapDTO map = seatMapService.getSeatMapForEvent(eventoId);
        return ResponseEntity.ok(map);
    }
}
