package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.repository.SeatSelectionRepository;
import com.mycompany.myapp.service.SeatSelectionService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SeatSelection}.
 */
@RestController
@RequestMapping("/api/seat-selections")
public class SeatSelectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SeatSelectionResource.class);

    private static final String ENTITY_NAME = "seatSelection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeatSelectionService seatSelectionService;

    private final SeatSelectionRepository seatSelectionRepository;

    public SeatSelectionResource(SeatSelectionService seatSelectionService, SeatSelectionRepository seatSelectionRepository) {
        this.seatSelectionService = seatSelectionService;
        this.seatSelectionRepository = seatSelectionRepository;
    }

    /**
     * {@code POST  /seat-selections} : Create a new seatSelection.
     *
     * @param seatSelection the seatSelection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seatSelection, or with status {@code 400 (Bad Request)} if the seatSelection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SeatSelection> createSeatSelection(@Valid @RequestBody SeatSelection seatSelection) throws URISyntaxException {
        LOG.debug("REST request to save SeatSelection : {}", seatSelection);
        if (seatSelection.getId() != null) {
            throw new BadRequestAlertException("A new seatSelection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        seatSelection = seatSelectionService.save(seatSelection);
        return ResponseEntity.created(new URI("/api/seat-selections/" + seatSelection.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, seatSelection.getId().toString()))
            .body(seatSelection);
    }

    /**
     * {@code PUT  /seat-selections/:id} : Updates an existing seatSelection.
     *
     * @param id the id of the seatSelection to save.
     * @param seatSelection the seatSelection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatSelection,
     * or with status {@code 400 (Bad Request)} if the seatSelection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seatSelection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SeatSelection> updateSeatSelection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SeatSelection seatSelection
    ) throws URISyntaxException {
        LOG.debug("REST request to update SeatSelection : {}, {}", id, seatSelection);
        if (seatSelection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatSelection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatSelectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        seatSelection = seatSelectionService.update(seatSelection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seatSelection.getId().toString()))
            .body(seatSelection);
    }

    /**
     * {@code PATCH  /seat-selections/:id} : Partial updates given fields of an existing seatSelection, field will ignore if it is null
     *
     * @param id the id of the seatSelection to save.
     * @param seatSelection the seatSelection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatSelection,
     * or with status {@code 400 (Bad Request)} if the seatSelection is not valid,
     * or with status {@code 404 (Not Found)} if the seatSelection is not found,
     * or with status {@code 500 (Internal Server Error)} if the seatSelection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeatSelection> partialUpdateSeatSelection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SeatSelection seatSelection
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SeatSelection partially : {}, {}", id, seatSelection);
        if (seatSelection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seatSelection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatSelectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeatSelection> result = seatSelectionService.partialUpdate(seatSelection);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seatSelection.getId().toString())
        );
    }

    /**
     * {@code GET  /seat-selections} : get all the seatSelections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seatSelections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SeatSelection>> getAllSeatSelections(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SeatSelections");
        Page<SeatSelection> page = seatSelectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seat-selections/:id} : get the "id" seatSelection.
     *
     * @param id the id of the seatSelection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seatSelection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SeatSelection> getSeatSelection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SeatSelection : {}", id);
        Optional<SeatSelection> seatSelection = seatSelectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seatSelection);
    }

    /**
     * {@code DELETE  /seat-selections/:id} : delete the "id" seatSelection.
     *
     * @param id the id of the seatSelection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatSelection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SeatSelection : {}", id);
        seatSelectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
