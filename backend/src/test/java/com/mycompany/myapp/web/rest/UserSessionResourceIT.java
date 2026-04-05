package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserSessionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserSession;
import com.mycompany.myapp.repository.UserSessionRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserSessionResourceIT {

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_STEP = 1;
    private static final Integer UPDATED_STEP = 2;

    private static final Long DEFAULT_EVENT_ID = 1L;
    private static final Long UPDATED_EVENT_ID = 2L;

    private static final String DEFAULT_ASIENTOS = "AAAAAAAAAA";
    private static final String UPDATED_ASIENTOS = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRES = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRES = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSessionMockMvc;

    private UserSession userSession;

    private UserSession insertedUserSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSession createEntity() {
        return new UserSession()
            .userLogin(DEFAULT_USER_LOGIN)
            .step(DEFAULT_STEP)
            .eventId(DEFAULT_EVENT_ID)
            .asientos(DEFAULT_ASIENTOS)
            .nombres(DEFAULT_NOMBRES)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSession createUpdatedEntity() {
        return new UserSession()
            .userLogin(UPDATED_USER_LOGIN)
            .step(UPDATED_STEP)
            .eventId(UPDATED_EVENT_ID)
            .asientos(UPDATED_ASIENTOS)
            .nombres(UPDATED_NOMBRES)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        userSession = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserSession != null) {
            userSessionRepository.delete(insertedUserSession);
            insertedUserSession = null;
        }
    }

    @Test
    @Transactional
    void createUserSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSession
        var returnedUserSession = om.readValue(
            restUserSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSession.class
        );

        // Validate the UserSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserSessionUpdatableFieldsEquals(returnedUserSession, getPersistedUserSession(returnedUserSession));

        insertedUserSession = returnedUserSession;
    }

    @Test
    @Transactional
    void createUserSessionWithExistingId() throws Exception {
        // Create the UserSession with an existing ID
        userSession.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isBadRequest());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSession.setUserLogin(null);

        // Create the UserSession, which fails.

        restUserSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSession.setStep(null);

        // Create the UserSession, which fails.

        restUserSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userSession.setUpdatedAt(null);

        // Create the UserSession, which fails.

        restUserSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserSessions() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        // Get all the userSessionList
        restUserSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].step").value(hasItem(DEFAULT_STEP)))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].asientos").value(hasItem(DEFAULT_ASIENTOS)))
            .andExpect(jsonPath("$.[*].nombres").value(hasItem(DEFAULT_NOMBRES)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getUserSession() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        // Get the userSession
        restUserSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, userSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSession.getId().intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.step").value(DEFAULT_STEP))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.asientos").value(DEFAULT_ASIENTOS))
            .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserSession() throws Exception {
        // Get the userSession
        restUserSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSession() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSession
        UserSession updatedUserSession = userSessionRepository.findById(userSession.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserSession are not directly saved in db
        em.detach(updatedUserSession);
        updatedUserSession
            .userLogin(UPDATED_USER_LOGIN)
            .step(UPDATED_STEP)
            .eventId(UPDATED_EVENT_ID)
            .asientos(UPDATED_ASIENTOS)
            .nombres(UPDATED_NOMBRES)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserSession))
            )
            .andExpect(status().isOk());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSessionToMatchAllProperties(updatedUserSession);
    }

    @Test
    @Transactional
    void putNonExistingUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSession.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSessionWithPatch() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSession using partial update
        UserSession partialUpdatedUserSession = new UserSession();
        partialUpdatedUserSession.setId(userSession.getId());

        partialUpdatedUserSession.eventId(UPDATED_EVENT_ID).nombres(UPDATED_NOMBRES).updatedAt(UPDATED_UPDATED_AT);

        restUserSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSession))
            )
            .andExpect(status().isOk());

        // Validate the UserSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSession, userSession),
            getPersistedUserSession(userSession)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSessionWithPatch() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSession using partial update
        UserSession partialUpdatedUserSession = new UserSession();
        partialUpdatedUserSession.setId(userSession.getId());

        partialUpdatedUserSession
            .userLogin(UPDATED_USER_LOGIN)
            .step(UPDATED_STEP)
            .eventId(UPDATED_EVENT_ID)
            .asientos(UPDATED_ASIENTOS)
            .nombres(UPDATED_NOMBRES)
            .updatedAt(UPDATED_UPDATED_AT);

        restUserSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSession))
            )
            .andExpect(status().isOk());

        // Validate the UserSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSessionUpdatableFieldsEquals(partialUpdatedUserSession, getPersistedUserSession(partialUpdatedUserSession));
    }

    @Test
    @Transactional
    void patchNonExistingUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSession))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSession.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSession)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSession() throws Exception {
        // Initialize the database
        insertedUserSession = userSessionRepository.saveAndFlush(userSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSession
        restUserSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSessionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserSession getPersistedUserSession(UserSession userSession) {
        return userSessionRepository.findById(userSession.getId()).orElseThrow();
    }

    protected void assertPersistedUserSessionToMatchAllProperties(UserSession expectedUserSession) {
        assertUserSessionAllPropertiesEquals(expectedUserSession, getPersistedUserSession(expectedUserSession));
    }

    protected void assertPersistedUserSessionToMatchUpdatableProperties(UserSession expectedUserSession) {
        assertUserSessionAllUpdatablePropertiesEquals(expectedUserSession, getPersistedUserSession(expectedUserSession));
    }
}
