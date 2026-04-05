package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SeatSelection.
 */
@Entity
@Table(name = "seat_selection")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeatSelection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "asientos", nullable = false)
    private String asientos;

    @NotNull
    @Column(name = "fecha_seleccion", nullable = false)
    private Instant fechaSeleccion;

    @NotNull
    @Column(name = "expiracion", nullable = false)
    private Instant expiracion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SeatSelection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public SeatSelection eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public SeatSelection userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getAsientos() {
        return this.asientos;
    }

    public SeatSelection asientos(String asientos) {
        this.setAsientos(asientos);
        return this;
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }

    public Instant getFechaSeleccion() {
        return this.fechaSeleccion;
    }

    public SeatSelection fechaSeleccion(Instant fechaSeleccion) {
        this.setFechaSeleccion(fechaSeleccion);
        return this;
    }

    public void setFechaSeleccion(Instant fechaSeleccion) {
        this.fechaSeleccion = fechaSeleccion;
    }

    public Instant getExpiracion() {
        return this.expiracion;
    }

    public SeatSelection expiracion(Instant expiracion) {
        this.setExpiracion(expiracion);
        return this;
    }

    public void setExpiracion(Instant expiracion) {
        this.expiracion = expiracion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatSelection)) {
            return false;
        }
        return getId() != null && getId().equals(((SeatSelection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatSelection{" +
            "id=" + getId() +
            ", eventId=" + getEventId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", asientos='" + getAsientos() + "'" +
            ", fechaSeleccion='" + getFechaSeleccion() + "'" +
            ", expiracion='" + getExpiracion() + "'" +
            "}";
    }
}
