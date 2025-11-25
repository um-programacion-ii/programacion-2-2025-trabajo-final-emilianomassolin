package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SeatSelectionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SeatSelection;
import com.mycompany.myapp.repository.SeatSelectionRepository;
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
 * Integration tests for the {@link SeatSelectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatSelectionResourceIT {

    private static final Long DEFAULT_EVENT_ID = 1L;
    private static final Long UPDATED_EVENT_ID = 2L;

    private static final String DEFAULT_USER_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_USER_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_ASIENTOS = "AAAAAAAAAA";
    private static final String UPDATED_ASIENTOS = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_SELECCION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SELECCION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/seat-selections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SeatSelectionRepository seatSelectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatSelectionMockMvc;

    private SeatSelection seatSelection;

    private SeatSelection insertedSeatSelection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatSelection createEntity() {
        return new SeatSelection()
            .eventId(DEFAULT_EVENT_ID)
            .userLogin(DEFAULT_USER_LOGIN)
            .asientos(DEFAULT_ASIENTOS)
            .fechaSeleccion(DEFAULT_FECHA_SELECCION)
            .expiracion(DEFAULT_EXPIRACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SeatSelection createUpdatedEntity() {
        return new SeatSelection()
            .eventId(UPDATED_EVENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .asientos(UPDATED_ASIENTOS)
            .fechaSeleccion(UPDATED_FECHA_SELECCION)
            .expiracion(UPDATED_EXPIRACION);
    }

    @BeforeEach
    void initTest() {
        seatSelection = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSeatSelection != null) {
            seatSelectionRepository.delete(insertedSeatSelection);
            insertedSeatSelection = null;
        }
    }

    @Test
    @Transactional
    void createSeatSelection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SeatSelection
        var returnedSeatSelection = om.readValue(
            restSeatSelectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SeatSelection.class
        );

        // Validate the SeatSelection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSeatSelectionUpdatableFieldsEquals(returnedSeatSelection, getPersistedSeatSelection(returnedSeatSelection));

        insertedSeatSelection = returnedSeatSelection;
    }

    @Test
    @Transactional
    void createSeatSelectionWithExistingId() throws Exception {
        // Create the SeatSelection with an existing ID
        seatSelection.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seatSelection.setEventId(null);

        // Create the SeatSelection, which fails.

        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seatSelection.setUserLogin(null);

        // Create the SeatSelection, which fails.

        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAsientosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seatSelection.setAsientos(null);

        // Create the SeatSelection, which fails.

        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaSeleccionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seatSelection.setFechaSeleccion(null);

        // Create the SeatSelection, which fails.

        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiracionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        seatSelection.setExpiracion(null);

        // Create the SeatSelection, which fails.

        restSeatSelectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeatSelections() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        // Get all the seatSelectionList
        restSeatSelectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seatSelection.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userLogin").value(hasItem(DEFAULT_USER_LOGIN)))
            .andExpect(jsonPath("$.[*].asientos").value(hasItem(DEFAULT_ASIENTOS)))
            .andExpect(jsonPath("$.[*].fechaSeleccion").value(hasItem(DEFAULT_FECHA_SELECCION.toString())))
            .andExpect(jsonPath("$.[*].expiracion").value(hasItem(DEFAULT_EXPIRACION.toString())));
    }

    @Test
    @Transactional
    void getSeatSelection() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        // Get the seatSelection
        restSeatSelectionMockMvc
            .perform(get(ENTITY_API_URL_ID, seatSelection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seatSelection.getId().intValue()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.userLogin").value(DEFAULT_USER_LOGIN))
            .andExpect(jsonPath("$.asientos").value(DEFAULT_ASIENTOS))
            .andExpect(jsonPath("$.fechaSeleccion").value(DEFAULT_FECHA_SELECCION.toString()))
            .andExpect(jsonPath("$.expiracion").value(DEFAULT_EXPIRACION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSeatSelection() throws Exception {
        // Get the seatSelection
        restSeatSelectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeatSelection() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seatSelection
        SeatSelection updatedSeatSelection = seatSelectionRepository.findById(seatSelection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeatSelection are not directly saved in db
        em.detach(updatedSeatSelection);
        updatedSeatSelection
            .eventId(UPDATED_EVENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .asientos(UPDATED_ASIENTOS)
            .fechaSeleccion(UPDATED_FECHA_SELECCION)
            .expiracion(UPDATED_EXPIRACION);

        restSeatSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeatSelection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSeatSelection))
            )
            .andExpect(status().isOk());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSeatSelectionToMatchAllProperties(updatedSeatSelection);
    }

    @Test
    @Transactional
    void putNonExistingSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatSelection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seatSelection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(seatSelection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatSelectionWithPatch() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seatSelection using partial update
        SeatSelection partialUpdatedSeatSelection = new SeatSelection();
        partialUpdatedSeatSelection.setId(seatSelection.getId());

        partialUpdatedSeatSelection
            .eventId(UPDATED_EVENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .asientos(UPDATED_ASIENTOS)
            .fechaSeleccion(UPDATED_FECHA_SELECCION);

        restSeatSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatSelection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeatSelection))
            )
            .andExpect(status().isOk());

        // Validate the SeatSelection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeatSelectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSeatSelection, seatSelection),
            getPersistedSeatSelection(seatSelection)
        );
    }

    @Test
    @Transactional
    void fullUpdateSeatSelectionWithPatch() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the seatSelection using partial update
        SeatSelection partialUpdatedSeatSelection = new SeatSelection();
        partialUpdatedSeatSelection.setId(seatSelection.getId());

        partialUpdatedSeatSelection
            .eventId(UPDATED_EVENT_ID)
            .userLogin(UPDATED_USER_LOGIN)
            .asientos(UPDATED_ASIENTOS)
            .fechaSeleccion(UPDATED_FECHA_SELECCION)
            .expiracion(UPDATED_EXPIRACION);

        restSeatSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeatSelection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeatSelection))
            )
            .andExpect(status().isOk());

        // Validate the SeatSelection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeatSelectionUpdatableFieldsEquals(partialUpdatedSeatSelection, getPersistedSeatSelection(partialUpdatedSeatSelection));
    }

    @Test
    @Transactional
    void patchNonExistingSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatSelection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seatSelection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(seatSelection))
            )
            .andExpect(status().isBadRequest());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeatSelection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        seatSelection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatSelectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(seatSelection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SeatSelection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeatSelection() throws Exception {
        // Initialize the database
        insertedSeatSelection = seatSelectionRepository.saveAndFlush(seatSelection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the seatSelection
        restSeatSelectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, seatSelection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return seatSelectionRepository.count();
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

    protected SeatSelection getPersistedSeatSelection(SeatSelection seatSelection) {
        return seatSelectionRepository.findById(seatSelection.getId()).orElseThrow();
    }

    protected void assertPersistedSeatSelectionToMatchAllProperties(SeatSelection expectedSeatSelection) {
        assertSeatSelectionAllPropertiesEquals(expectedSeatSelection, getPersistedSeatSelection(expectedSeatSelection));
    }

    protected void assertPersistedSeatSelectionToMatchUpdatableProperties(SeatSelection expectedSeatSelection) {
        assertSeatSelectionAllUpdatablePropertiesEquals(expectedSeatSelection, getPersistedSeatSelection(expectedSeatSelection));
    }
}
