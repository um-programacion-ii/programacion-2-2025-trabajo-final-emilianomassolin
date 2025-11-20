package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Sale.
 */
@Entity
@Table(name = "sale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "venta_id", nullable = false)
    private Long ventaId;

    @NotNull
    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @NotNull
    @Column(name = "asientos", nullable = false)
    private String asientos;

    @NotNull
    @Column(name = "nombres", nullable = false)
    private String nombres;

    @NotNull
    @Column(name = "total", nullable = false)
    private Double total;

    @NotNull
    @Column(name = "estado", nullable = false)
    private String estado;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Sale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaId() {
        return this.ventaId;
    }

    public Sale ventaId(Long ventaId) {
        this.setVentaId(ventaId);
        return this;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public Sale eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Instant getFechaVenta() {
        return this.fechaVenta;
    }

    public Sale fechaVenta(Instant fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getAsientos() {
        return this.asientos;
    }

    public Sale asientos(String asientos) {
        this.setAsientos(asientos);
        return this;
    }

    public void setAsientos(String asientos) {
        this.asientos = asientos;
    }

    public String getNombres() {
        return this.nombres;
    }

    public Sale nombres(String nombres) {
        this.setNombres(nombres);
        return this;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public Double getTotal() {
        return this.total;
    }

    public Sale total(Double total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return this.estado;
    }

    public Sale estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sale)) {
            return false;
        }
        return getId() != null && getId().equals(((Sale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sale{" +
            "id=" + getId() +
            ", ventaId=" + getVentaId() +
            ", eventId=" + getEventId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", asientos='" + getAsientos() + "'" +
            ", nombres='" + getNombres() + "'" +
            ", total=" + getTotal() +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
