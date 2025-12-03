package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Event}.
 */
@Service
@Transactional
public class EventService {

    private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Save a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    public Event save(Event event) {
        LOG.debug("Request to save Event : {}", event);
        return eventRepository.save(event);
    }

    /**
     * Update a event.
     *
     * @param event the entity to save.
     * @return the persisted entity.
     */
    public Event update(Event event) {
        LOG.debug("Request to update Event : {}", event);
        return eventRepository.save(event);
    }

    /**
     * Partially update a event.
     *
     * @param event the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Event> partialUpdate(Event event) {
        LOG.debug("Request to partially update Event : {}", event);

        return eventRepository
            .findById(event.getId())
            .map(existingEvent -> {
                if (event.getEventId() != null) {
                    existingEvent.setEventId(event.getEventId());
                }
                if (event.getTitulo() != null) {
                    existingEvent.setTitulo(event.getTitulo());
                }
                if (event.getResumen() != null) {
                    existingEvent.setResumen(event.getResumen());
                }
                if (event.getFecha() != null) {
                    existingEvent.setFecha(event.getFecha());
                }
                if (event.getDescripcion() != null) {
                    existingEvent.setDescripcion(event.getDescripcion());
                }
                if (event.getFilaAsientos() != null) {
                    existingEvent.setFilaAsientos(event.getFilaAsientos());
                }
                if (event.getColumnaAsientos() != null) {
                    existingEvent.setColumnaAsientos(event.getColumnaAsientos());
                }
                if (event.getTipoEvento() != null) {
                    existingEvent.setTipoEvento(event.getTipoEvento());
                }
                if (event.getDireccion() != null) {
                    existingEvent.setDireccion(event.getDireccion());
                }
                if (event.getImagen() != null) {
                    existingEvent.setImagen(event.getImagen());
                }
                if (event.getPrecioEntrada() != null) {
                    existingEvent.setPrecioEntrada(event.getPrecioEntrada());
                }
                if (event.getTipoNombre() != null) {
                    existingEvent.setTipoNombre(event.getTipoNombre());
                }
                if (event.getTipoDescripcion() != null) {
                    existingEvent.setTipoDescripcion(event.getTipoDescripcion());
                }
                if (event.getIntegrantes() != null) {
                    existingEvent.setIntegrantes(event.getIntegrantes());
                }

                return existingEvent;
            })
            .map(eventRepository::save);
    }

    /**
     * Get all the events.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Event> findAll(Pageable pageable) {
        LOG.debug("Request to get all Events");
        return eventRepository.findAll(pageable);
    }

    /**
     * Get one event by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Event> findOne(Long id) {
        LOG.debug("Request to get Event : {}", id);
        return eventRepository.findById(id);
    }

    /**
     * Delete the event by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Event : {}", id);
        eventRepository.deleteById(id);
    }
    /**
     * Get one event by eventId (ID de la cátedra).
     *
     * @param eventId the eventId of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Event> findByEventId(Long eventId) {
        LOG.debug("Request to get Event by eventId : {}", eventId);
        return eventRepository.findByEventId(eventId);
    }

}
