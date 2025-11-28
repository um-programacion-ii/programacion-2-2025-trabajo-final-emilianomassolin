package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserSession;
import com.mycompany.myapp.repository.UserSessionRepository;
import com.mycompany.myapp.service.UserSessionService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserSession}.
 */
@RestController
@RequestMapping("/api/user-sessions")
public class UserSessionResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSessionResource.class);

    private static final String ENTITY_NAME = "userSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSessionService userSessionService;

    private final UserSessionRepository userSessionRepository;

    public UserSessionResource(UserSessionService userSessionService, UserSessionRepository userSessionRepository) {
        this.userSessionService = userSessionService;
        this.userSessionRepository = userSessionRepository;
    }

    /**
     * {@code POST  /user-sessions} : Create a new userSession.
     *
     * @param userSession the userSession to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSession, or with status {@code 400 (Bad Request)} if the userSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSession> createUserSession(@Valid @RequestBody UserSession userSession) throws URISyntaxException {
        LOG.debug("REST request to save UserSession : {}", userSession);
        if (userSession.getId() != null) {
            throw new BadRequestAlertException("A new userSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSession = userSessionService.save(userSession);
        return ResponseEntity.created(new URI("/api/user-sessions/" + userSession.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userSession.getId().toString()))
            .body(userSession);
    }

    /**
     * {@code PUT  /user-sessions/:id} : Updates an existing userSession.
     *
     * @param id the id of the userSession to save.
     * @param userSession the userSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSession,
     * or with status {@code 400 (Bad Request)} if the userSession is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSession> updateUserSession(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserSession userSession
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSession : {}, {}", id, userSession);
        if (userSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSession = userSessionService.update(userSession);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSession.getId().toString()))
            .body(userSession);
    }

    /**
     * {@code PATCH  /user-sessions/:id} : Partial updates given fields of an existing userSession, field will ignore if it is null
     *
     * @param id the id of the userSession to save.
     * @param userSession the userSession to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSession,
     * or with status {@code 400 (Bad Request)} if the userSession is not valid,
     * or with status {@code 404 (Not Found)} if the userSession is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSession couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSession> partialUpdateUserSession(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserSession userSession
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSession partially : {}, {}", id, userSession);
        if (userSession.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSession.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSessionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSession> result = userSessionService.partialUpdate(userSession);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userSession.getId().toString())
        );
    }

    /**
     * {@code GET  /user-sessions} : get all the userSessions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSessions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserSession>> getAllUserSessions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserSessions");
        Page<UserSession> page = userSessionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-sessions/:id} : get the "id" userSession.
     *
     * @param id the id of the userSession to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSession, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSession> getUserSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserSession : {}", id);
        Optional<UserSession> userSession = userSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userSession);
    }

    /**
     * {@code DELETE  /user-sessions/:id} : delete the "id" userSession.
     *
     * @param id the id of the userSession to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSession(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserSession : {}", id);
        userSessionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
