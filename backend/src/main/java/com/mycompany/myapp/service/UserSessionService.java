package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserSession;
import com.mycompany.myapp.repository.UserSessionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserSession}.
 */
@Service
@Transactional
public class UserSessionService {

    private static final Logger LOG = LoggerFactory.getLogger(UserSessionService.class);

    private final UserSessionRepository userSessionRepository;

    public UserSessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    /**
     * Save a userSession.
     *
     * @param userSession the entity to save.
     * @return the persisted entity.
     */
    public UserSession save(UserSession userSession) {
        LOG.debug("Request to save UserSession : {}", userSession);
        return userSessionRepository.save(userSession);
    }

    /**
     * Update a userSession.
     *
     * @param userSession the entity to save.
     * @return the persisted entity.
     */
    public UserSession update(UserSession userSession) {
        LOG.debug("Request to update UserSession : {}", userSession);
        return userSessionRepository.save(userSession);
    }

    /**
     * Partially update a userSession.
     *
     * @param userSession the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserSession> partialUpdate(UserSession userSession) {
        LOG.debug("Request to partially update UserSession : {}", userSession);

        return userSessionRepository
            .findById(userSession.getId())
            .map(existingUserSession -> {
                if (userSession.getUserLogin() != null) {
                    existingUserSession.setUserLogin(userSession.getUserLogin());
                }
                if (userSession.getStep() != null) {
                    existingUserSession.setStep(userSession.getStep());
                }
                if (userSession.getEventId() != null) {
                    existingUserSession.setEventId(userSession.getEventId());
                }
                if (userSession.getAsientos() != null) {
                    existingUserSession.setAsientos(userSession.getAsientos());
                }
                if (userSession.getNombres() != null) {
                    existingUserSession.setNombres(userSession.getNombres());
                }
                if (userSession.getUpdatedAt() != null) {
                    existingUserSession.setUpdatedAt(userSession.getUpdatedAt());
                }

                return existingUserSession;
            })
            .map(userSessionRepository::save);
    }

    /**
     * Get all the userSessions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserSession> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserSessions");
        return userSessionRepository.findAll(pageable);
    }

    /**
     * Get one userSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserSession> findOne(Long id) {
        LOG.debug("Request to get UserSession : {}", id);
        return userSessionRepository.findById(id);
    }

    /**
     * Delete the userSession by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserSession : {}", id);
        userSessionRepository.deleteById(id);
    }
}
