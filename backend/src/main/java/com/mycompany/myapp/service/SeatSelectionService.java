package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.repository.SeatSelectionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SeatSelection}.
 */
@Service
@Transactional
public class SeatSelectionService {

    private static final Logger LOG = LoggerFactory.getLogger(SeatSelectionService.class);

    private final SeatSelectionRepository seatSelectionRepository;

    public SeatSelectionService(SeatSelectionRepository seatSelectionRepository) {
        this.seatSelectionRepository = seatSelectionRepository;
    }

    /**
     * Save a seatSelection.
     *
     * @param seatSelection the entity to save.
     * @return the persisted entity.
     */
    public SeatSelection save(SeatSelection seatSelection) {
        LOG.debug("Request to save SeatSelection : {}", seatSelection);
        return seatSelectionRepository.save(seatSelection);
    }

    /**
     * Update a seatSelection.
     *
     * @param seatSelection the entity to save.
     * @return the persisted entity.
     */
    public SeatSelection update(SeatSelection seatSelection) {
        LOG.debug("Request to update SeatSelection : {}", seatSelection);
        return seatSelectionRepository.save(seatSelection);
    }

    /**
     * Partially update a seatSelection.
     *
     * @param seatSelection the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SeatSelection> partialUpdate(SeatSelection seatSelection) {
        LOG.debug("Request to partially update SeatSelection : {}", seatSelection);

        return seatSelectionRepository
            .findById(seatSelection.getId())
            .map(existingSeatSelection -> {
                if (seatSelection.getEventId() != null) {
                    existingSeatSelection.setEventId(seatSelection.getEventId());
                }
                if (seatSelection.getUserLogin() != null) {
                    existingSeatSelection.setUserLogin(seatSelection.getUserLogin());
                }
                if (seatSelection.getAsientos() != null) {
                    existingSeatSelection.setAsientos(seatSelection.getAsientos());
                }
                if (seatSelection.getFechaSeleccion() != null) {
                    existingSeatSelection.setFechaSeleccion(seatSelection.getFechaSeleccion());
                }
                if (seatSelection.getExpiracion() != null) {
                    existingSeatSelection.setExpiracion(seatSelection.getExpiracion());
                }

                return existingSeatSelection;
            })
            .map(seatSelectionRepository::save);
    }

    /**
     * Get all the seatSelections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SeatSelection> findAll(Pageable pageable) {
        LOG.debug("Request to get all SeatSelections");
        return seatSelectionRepository.findAll(pageable);
    }

    /**
     * Get one seatSelection by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SeatSelection> findOne(Long id) {
        LOG.debug("Request to get SeatSelection : {}", id);
        return seatSelectionRepository.findById(id);
    }

    /**
     * Delete the seatSelection by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SeatSelection : {}", id);
        seatSelectionRepository.deleteById(id);
    }
}
