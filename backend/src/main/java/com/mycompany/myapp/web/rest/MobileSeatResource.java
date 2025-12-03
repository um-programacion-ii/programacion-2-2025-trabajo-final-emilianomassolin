package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
import com.mycompany.myapp.service.ProxySeatService;
import com.mycompany.myapp.service.dto.proxy.SeatMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@RestController
@RequestMapping("/api/mobile")
public class MobileSeatResource {

    private final Logger log = LoggerFactory.getLogger(MobileSeatResource.class);

    private final ProxySeatService proxySeatService;
    private final EventRepository eventRepository;

    public MobileSeatResource(ProxySeatService proxySeatService, EventRepository eventRepository) {
        this.proxySeatService = proxySeatService;
        this.eventRepository = eventRepository;
    }

    /**
     * GET /api/mobile/eventos/{eventId}/asientos
     *
     * ATENCIÓN: eventId es el ID de la cátedra (Event.eventId), NO el PK interno (Event.id).
     */
    @GetMapping("/eventos/{eventId}/asientos")
    public ResponseEntity<SeatMapDTO> getSeatMap(@PathVariable Long eventId) {
        log.debug("REST request to get seat map for eventId (cátedra) {}", eventId);

        Optional<Event> eventOpt = eventRepository.findOneByEventId(eventId);
        if (eventOpt.isEmpty()) {
            log.warn("No se encontró Event local con eventId={}", eventId);
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        Integer filas = event.getFilaAsientos();
        Integer columnas = event.getColumnaAsientos();

        if (filas == null || columnas == null) {
            log.warn("Event {} no tiene filaAsientos/columnaAsientos seteados", eventId);
            return ResponseEntity.badRequest().build();
        }

        try {
            SeatMapDTO map = proxySeatService.getSeatMapForEvent(eventId, filas, columnas);
            return ResponseEntity.ok(map);
        } catch (RestClientException e) {
            log.error("No se pudo obtener el mapa de asientos desde el proxy para eventId={}", eventId, e);
            return ResponseEntity.status(503).build(); // Service Unavailable
        }
    }
}
