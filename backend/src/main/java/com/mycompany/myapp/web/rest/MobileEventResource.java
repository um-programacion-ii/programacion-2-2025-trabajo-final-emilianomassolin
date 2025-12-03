package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.service.EventService;
import com.mycompany.myapp.service.dto.mobile.MobileEventSummaryDTO;
import com.mycompany.myapp.service.dto.mobile.MobileEventDetailDTO;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints mobile para eventos.
 *
 * /api/mobile/eventos        -> lista resumida (desde DB local)
 * /api/mobile/eventos/{id}   -> detalle completo (usando eventId de la cátedra)
 */
@RestController
@RequestMapping("/api/mobile")
public class MobileEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(MobileEventResource.class);

    private final EventService eventService;

    public MobileEventResource(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * GET  /eventos : devuelve lista resumida de eventos para el mobile
     * leyendo de la tabla local event.
     */
    @GetMapping("/eventos")
    public List<MobileEventSummaryDTO> listarEventosResumidos() {
        LOG.debug("API mobile: listar eventos resumidos (desde DB local)");

        // Traemos TODOS los eventos sin paginar
        Pageable unpaged = Pageable.unpaged();
        List<Event> eventos = eventService.findAll(unpaged).getContent();

        return eventos.stream()
            .map(this::toSummaryDTO)
            .collect(Collectors.toList());
    }

    /**
     * GET  /eventos/{id} : devuelve datos completos de un evento,
     * usando el eventId de la cátedra (no el id autogenerado).
     *
     * O sea: /api/mobile/eventos/1 -> busca event.eventId = 1
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<MobileEventDetailDTO> obtenerEvento(@PathVariable Long id) {
        LOG.debug("API mobile: obtener evento completo (desde DB local) eventId={}", id);

        Optional<Event> opt = eventService.findByEventId(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event evento = opt.get();
        MobileEventDetailDTO dto = toDetailDTO(evento);

        return ResponseEntity.ok(dto);
    }

    // ===================== Mappers =====================

    private MobileEventSummaryDTO toSummaryDTO(Event e) {
        MobileEventSummaryDTO dto = new MobileEventSummaryDTO();
        dto.setEventId(e.getEventId());
        dto.setTitulo(e.getTitulo());
        dto.setResumen(e.getResumen());
        dto.setFecha(e.getFecha());
        dto.setPrecioEntrada(e.getPrecioEntrada());
        dto.setTipoNombre(e.getTipoNombre());
        dto.setTipoDescripcion(e.getTipoDescripcion());
        dto.setImagen(e.getImagen());
        dto.setDireccion(e.getDireccion());
        return dto;
    }

    private MobileEventDetailDTO toDetailDTO(Event e) {
        MobileEventDetailDTO dto = new MobileEventDetailDTO();
        dto.setEventId(e.getEventId());
        dto.setTitulo(e.getTitulo());
        dto.setResumen(e.getResumen());
        dto.setFecha(e.getFecha());
        dto.setDescripcion(e.getDescripcion());
        dto.setFilaAsientos(e.getFilaAsientos());
        dto.setColumnaAsientos(e.getColumnaAsientos());
        dto.setTipoEvento(e.getTipoEvento());
        dto.setDireccion(e.getDireccion());
        dto.setImagen(e.getImagen());
        dto.setPrecioEntrada(e.getPrecioEntrada());
        dto.setTipoNombre(e.getTipoNombre());
        dto.setTipoDescripcion(e.getTipoDescripcion());
        dto.setIntegrantes(e.getIntegrantes()); // es String en tu entidad
        return dto;
    }
}
