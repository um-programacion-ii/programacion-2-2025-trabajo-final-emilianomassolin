package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserSession.
 */
@Entity
@Table(name = "user_session")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_login", nullable = false)
    private String userLogin;

    @NotNull
    @Column(name = "step", nullable = false)
    private Integer step;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "asientos")
    private String asientos;

    @Column(name = "nombres")
    private String nombres;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return this.userLogin;
    }

    public UserSession userLogin(String userLogin) {
        this.setUserLogin(userLogin);
        return this;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Integer getStep() {
        return this.step;
    }

    public UserSession step(Integer step) {
        this.setStep(step);
        return this;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public UserSession eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getAsientos() {
        return this.asientos;
    }

    public UserSession asientos(String asientos) {
        this.setAsientos(asientos);
        return this;
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }

    public String getNombres() {
        return this.nombres;
    }

    public UserSession nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public UserSession updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSession)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSession{" +
            "id=" + getId() +
            ", userLogin='" + getUserLogin() + "'" +
            ", step=" + getStep() +
            ", eventId=" + getEventId() +
            ", asientos='" + getAsientos() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
