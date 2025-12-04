package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.EventAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.repository.EventRepository;
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
 * Integration tests for the {@link EventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventResourceIT {

    private static final Long DEFAULT_EVENT_ID = 1L;
    private static final Long UPDATED_EVENT_ID = 2L;

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_RESUMEN = "AAAAAAAAAA";
    private static final String UPDATED_RESUMEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Integer DEFAULT_FILA_ASIENTOS = 1;
    private static final Integer UPDATED_FILA_ASIENTOS = 2;

    private static final Integer DEFAULT_COLUMNA_ASIENTOS = 1;
    private static final Integer UPDATED_COLUMNA_ASIENTOS = 2;

    private static final String DEFAULT_TIPO_EVENTO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_EVENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_IMAGEN = "AAAAAAAAAA";
    private static final String UPDATED_IMAGEN = "BBBBBBBBBB";

    private static final Double DEFAULT_PRECIO_ENTRADA = 1D;
    private static final Double UPDATED_PRECIO_ENTRADA = 2D;

    private static final String DEFAULT_TIPO_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_INTEGRANTES = "AAAAAAAAAA";
    private static final String UPDATED_INTEGRANTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    private Event insertedEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity() {
        return new Event()
            .eventId(DEFAULT_EVENT_ID)
            .titulo(DEFAULT_TITULO)
            .resumen(DEFAULT_RESUMEN)
            .fecha(DEFAULT_FECHA)
            .descripcion(DEFAULT_DESCRIPCION)
            .filaAsientos(DEFAULT_FILA_ASIENTOS)
            .columnaAsientos(DEFAULT_COLUMNA_ASIENTOS)
            .tipoEvento(DEFAULT_TIPO_EVENTO)
            .direccion(DEFAULT_DIRECCION)
            .imagen(DEFAULT_IMAGEN)
            .precioEntrada(DEFAULT_PRECIO_ENTRADA)
            .tipoNombre(DEFAULT_TIPO_NOMBRE)
            .tipoDescripcion(DEFAULT_TIPO_DESCRIPCION)
            .integrantes(DEFAULT_INTEGRANTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity() {
        return new Event()
            .eventId(UPDATED_EVENT_ID)
            .titulo(UPDATED_TITULO)
            .resumen(UPDATED_RESUMEN)
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .filaAsientos(UPDATED_FILA_ASIENTOS)
            .columnaAsientos(UPDATED_COLUMNA_ASIENTOS)
            .tipoEvento(UPDATED_TIPO_EVENTO)
            .direccion(UPDATED_DIRECCION)
            .imagen(UPDATED_IMAGEN)
            .precioEntrada(UPDATED_PRECIO_ENTRADA)
            .tipoNombre(UPDATED_TIPO_NOMBRE)
            .tipoDescripcion(UPDATED_TIPO_DESCRIPCION)
            .integrantes(UPDATED_INTEGRANTES);
    }

    @BeforeEach
    void initTest() {
        event = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEvent != null) {
            eventRepository.delete(insertedEvent);
            insertedEvent = null;
        }
    }

    @Test
    @Transactional
    void createEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Event
        var returnedEvent = om.readValue(
            restEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Event.class
        );

        // Validate the Event in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEventUpdatableFieldsEquals(returnedEvent, getPersistedEvent(returnedEvent));

        insertedEvent = returnedEvent;
    }

    @Test
    @Transactional
    void createEventWithExistingId() throws Exception {
        // Create the Event with an existing ID
        event.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setEventId(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setTitulo(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setFecha(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilaAsientosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setFilaAsientos(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkColumnaAsientosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setColumnaAsientos(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrecioEntradaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setPrecioEntrada(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setTipoNombre(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDescripcionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        event.setTipoDescripcion(null);

        // Create the Event, which fails.

        restEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventId").value(hasItem(DEFAULT_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].resumen").value(hasItem(DEFAULT_RESUMEN)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].filaAsientos").value(hasItem(DEFAULT_FILA_ASIENTOS)))
            .andExpect(jsonPath("$.[*].columnaAsientos").value(hasItem(DEFAULT_COLUMNA_ASIENTOS)))
            .andExpect(jsonPath("$.[*].tipoEvento").value(hasItem(DEFAULT_TIPO_EVENTO)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].imagen").value(hasItem(DEFAULT_IMAGEN)))
            .andExpect(jsonPath("$.[*].precioEntrada").value(hasItem(DEFAULT_PRECIO_ENTRADA)))
            .andExpect(jsonPath("$.[*].tipoNombre").value(hasItem(DEFAULT_TIPO_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipoDescripcion").value(hasItem(DEFAULT_TIPO_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].integrantes").value(hasItem(DEFAULT_INTEGRANTES)));
    }

    @Test
    @Transactional
    void getEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc
            .perform(get(ENTITY_API_URL_ID, event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.eventId").value(DEFAULT_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.resumen").value(DEFAULT_RESUMEN))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.filaAsientos").value(DEFAULT_FILA_ASIENTOS))
            .andExpect(jsonPath("$.columnaAsientos").value(DEFAULT_COLUMNA_ASIENTOS))
            .andExpect(jsonPath("$.tipoEvento").value(DEFAULT_TIPO_EVENTO))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.imagen").value(DEFAULT_IMAGEN))
            .andExpect(jsonPath("$.precioEntrada").value(DEFAULT_PRECIO_ENTRADA))
            .andExpect(jsonPath("$.tipoNombre").value(DEFAULT_TIPO_NOMBRE))
            .andExpect(jsonPath("$.tipoDescripcion").value(DEFAULT_TIPO_DESCRIPCION))
            .andExpect(jsonPath("$.integrantes").value(DEFAULT_INTEGRANTES));
    }

    @Test
    @Transactional
    void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .eventId(UPDATED_EVENT_ID)
            .titulo(UPDATED_TITULO)
            .resumen(UPDATED_RESUMEN)
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .filaAsientos(UPDATED_FILA_ASIENTOS)
            .columnaAsientos(UPDATED_COLUMNA_ASIENTOS)
            .tipoEvento(UPDATED_TIPO_EVENTO)
            .direccion(UPDATED_DIRECCION)
            .imagen(UPDATED_IMAGEN)
            .precioEntrada(UPDATED_PRECIO_ENTRADA)
            .tipoNombre(UPDATED_TIPO_NOMBRE)
            .tipoDescripcion(UPDATED_TIPO_DESCRIPCION)
            .integrantes(UPDATED_INTEGRANTES);

        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEventToMatchAllProperties(updatedEvent);
    }

    @Test
    @Transactional
    void putNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL_ID, event.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(event)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventId(UPDATED_EVENT_ID)
            .titulo(UPDATED_TITULO)
            .resumen(UPDATED_RESUMEN)
            .descripcion(UPDATED_DESCRIPCION)
            .filaAsientos(UPDATED_FILA_ASIENTOS)
            .direccion(UPDATED_DIRECCION)
            .imagen(UPDATED_IMAGEN)
            .tipoNombre(UPDATED_TIPO_NOMBRE);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEvent, event), getPersistedEvent(event));
    }

    @Test
    @Transactional
    void fullUpdateEventWithPatch() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the event using partial update
        Event partialUpdatedEvent = new Event();
        partialUpdatedEvent.setId(event.getId());

        partialUpdatedEvent
            .eventId(UPDATED_EVENT_ID)
            .titulo(UPDATED_TITULO)
            .resumen(UPDATED_RESUMEN)
            .fecha(UPDATED_FECHA)
            .descripcion(UPDATED_DESCRIPCION)
            .filaAsientos(UPDATED_FILA_ASIENTOS)
            .columnaAsientos(UPDATED_COLUMNA_ASIENTOS)
            .tipoEvento(UPDATED_TIPO_EVENTO)
            .direccion(UPDATED_DIRECCION)
            .imagen(UPDATED_IMAGEN)
            .precioEntrada(UPDATED_PRECIO_ENTRADA)
            .tipoNombre(UPDATED_TIPO_NOMBRE)
            .tipoDescripcion(UPDATED_TIPO_DESCRIPCION)
            .integrantes(UPDATED_INTEGRANTES);

        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEvent))
            )
            .andExpect(status().isOk());

        // Validate the Event in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEventUpdatableFieldsEquals(partialUpdatedEvent, getPersistedEvent(partialUpdatedEvent));
    }

    @Test
    @Transactional
    void patchNonExistingEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, event.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(event))
            )
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        event.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(event)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Event in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvent() throws Exception {
        // Initialize the database
        insertedEvent = eventRepository.saveAndFlush(event);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the event
        restEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, event.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return eventRepository.count();
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

    protected Event getPersistedEvent(Event event) {
        return eventRepository.findById(event.getId()).orElseThrow();
    }

    protected void assertPersistedEventToMatchAllProperties(Event expectedEvent) {
        assertEventAllPropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }

    protected void assertPersistedEventToMatchUpdatableProperties(Event expectedEvent) {
        assertEventAllUpdatablePropertiesEquals(expectedEvent, getPersistedEvent(expectedEvent));
    }
}
