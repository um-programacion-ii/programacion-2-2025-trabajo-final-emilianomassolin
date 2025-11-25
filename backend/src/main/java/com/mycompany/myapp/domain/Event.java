package com.mycompany.myapp.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

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
    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "resumen")
    private String resumen;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private Instant fecha;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fila_asientos", nullable = false)
    private Integer filaAsientos;

    @NotNull
    @Column(name = "columna_asientos", nullable = false)
    private Integer columnaAsientos;

    @Column(name = "tipo_evento")
    private String tipoEvento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "imagen")
    private String imagen;

    @NotNull
    @Column(name = "precio_entrada", nullable = false)
    private Double precioEntrada;

    @NotNull
    @Column(name = "tipo_nombre", nullable = false)
    private String tipoNombre;

    @NotNull
    @Column(name = "tipo_descripcion", nullable = false)
    private String tipoDescripcion;

    @Column(name = "integrantes")
    private String integrantes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public Event eventId(Long eventId) {
        this.setEventId(eventId);
        return this;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Event titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return this.resumen;
    }

    public Event resumen(String resumen) {
        this.setResumen(resumen);
        return this;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Instant getFecha() {
        return this.fecha;
    }

    public Event fecha(Instant fecha) {
        this.setFecha(fecha);
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Event descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getFilaAsientos() {
        return this.filaAsientos;
    }

    public Event filaAsientos(Integer filaAsientos) {
        this.setFilaAsientos(filaAsientos);
        return this;
    }

    public void setFilaAsientos(Integer filaAsientos) {
        this.filaAsientos = filaAsientos;
    }

    public Integer getColumnaAsientos() {
        return this.columnaAsientos;
    }

    public Event columnaAsientos(Integer columnaAsientos) {
        this.setColumnaAsientos(columnaAsientos);
        return this;
    }

    public void setColumnaAsientos(Integer columnaAsientos) {
        this.columnaAsientos = columnaAsientos;
    }

    public String getTipoEvento() {
        return this.tipoEvento;
    }

    public Event tipoEvento(String tipoEvento) {
        this.setTipoEvento(tipoEvento);
        return this;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public Event direccion(String direccion) {
        this.setDireccion(direccion);
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return this.imagen;
    }

    public Event imagen(String imagen) {
        this.setImagen(imagen);
        return this;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getPrecioEntrada() {
        return this.precioEntrada;
    }

    public Event precioEntrada(Double precioEntrada) {
        this.setPrecioEntrada(precioEntrada);
        return this;
    }

    public void setPrecioEntrada(Double precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public String getTipoNombre() {
        return this.tipoNombre;
    }

    public Event tipoNombre(String tipoNombre) {
        this.setTipoNombre(tipoNombre);
        return this;
    }

    public void setTipoNombre(String tipoNombre) {
        this.tipoNombre = tipoNombre;
    }

    public String getTipoDescripcion() {
        return this.tipoDescripcion;
    }

    public Event tipoDescripcion(String tipoDescripcion) {
        this.setTipoDescripcion(tipoDescripcion);
        return this;
    }

    public void setTipoDescripcion(String tipoDescripcion) {
        this.tipoDescripcion = tipoDescripcion;
    }

    public String getIntegrantes() {
        return this.integrantes;
    }

    public Event integrantes(String integrantes) {
        this.setIntegrantes(integrantes);
        return this;
    }

    public void setIntegrantes(String integrantes) {
        this.integrantes = integrantes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return getId() != null && getId().equals(((Event) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", eventId=" + getEventId() +
            ", titulo='" + getTitulo() + "'" +
            ", resumen='" + getResumen() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", filaAsientos=" + getFilaAsientos() +
            ", columnaAsientos=" + getColumnaAsientos() +
            ", tipoEvento='" + getTipoEvento() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", imagen='" + getImagen() + "'" +
            ", precioEntrada=" + getPrecioEntrada() +
            ", tipoNombre='" + getTipoNombre() + "'" +
            ", tipoDescripcion='" + getTipoDescripcion() + "'" +
            ", integrantes='" + getIntegrantes() + "'" +
            "}";
    }
}
