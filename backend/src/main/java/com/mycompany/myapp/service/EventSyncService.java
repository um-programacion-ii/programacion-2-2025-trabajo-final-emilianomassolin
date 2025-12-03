package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
import com.mycompany.myapp.service.dto.catedra.EventoCompletoDTO;
import com.mycompany.myapp.service.dto.catedra.EventoCompletoDTO.IntegranteDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EventSyncService {

    private final Logger log = LoggerFactory.getLogger(EventSyncService.class);

    private final EventRepository eventRepository;
    private final CatedraService catedraService;

    public EventSyncService(EventRepository eventRepository, CatedraService catedraService) {
        this.eventRepository = eventRepository;
        this.catedraService = catedraService;
    }

    public List<Event> syncFromCatedra() {
        log.info("Sincronizando eventos desde cátedra...");

        List<EventoCompletoDTO> eventosCatedra = catedraService.obtenerEventosCompletos();
        Instant ahora = Instant.now();

        for (EventoCompletoDTO dto : eventosCatedra) {
            Long catedraId = dto.getId();
            if (catedraId == null) {
                continue;
            }

            Optional<Event> opt = eventRepository.findByEventId(catedraId);
            Event event = opt.orElseGet(Event::new);

            if (event.getId() == null) {
                event.setEventId(catedraId);
            }

            event.setTitulo(dto.getTitulo());
            event.setResumen(dto.getResumen());
            event.setDescripcion(dto.getDescripcion());
            event.setFecha(dto.getFecha());
            event.setFilaAsientos(dto.getFilaAsientos());
            event.setColumnaAsientos(dto.getColumnAsientos());
            event.setDireccion(dto.getDireccion());
            event.setImagen(dto.getImagen());

            if (dto.getPrecioEntrada() != null) {
                event.setPrecioEntrada(dto.getPrecioEntrada().doubleValue());
            }

            if (dto.getEventoTipo() != null) {
                event.setTipoNombre(dto.getEventoTipo().getNombre());
                event.setTipoDescripcion(dto.getEventoTipo().getDescripcion());
            }

            // Integrantes: lo dejamos como un String "ident nombre apellido | ..."
            event.setIntegrantes(mapIntegrantes(dto.getIntegrantes()));

            eventRepository.save(event);
        }

        return eventRepository.findAll();
    }

    private String mapIntegrantes(List<IntegranteDTO> integrantes) {
        if (integrantes == null || integrantes.isEmpty()) {
            return null;
        }

        return integrantes
            .stream()
            .map(i -> {
                StringBuilder sb = new StringBuilder();
                if (i.getIdentificacion() != null) {
                    sb.append(i.getIdentificacion()).append(" ");
                }
                if (i.getNombre() != null) {
                    sb.append(i.getNombre()).append(" ");
                }
                if (i.getApellido() != null) {
                    sb.append(i.getApellido());
                }
                return sb.toString().trim();
            })
            .collect(Collectors.joining(" | "));
    }
}
