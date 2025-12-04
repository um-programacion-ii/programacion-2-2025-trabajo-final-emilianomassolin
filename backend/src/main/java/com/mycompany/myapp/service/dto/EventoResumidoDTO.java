package com.mycompany.myapp.service.dto.catedra;

import java.math.BigDecimal;
import java.time.Instant;

public class EventoResumidoDTO {

    private String titulo;
    private String resumen;
    private String descripcion;
    private Instant fecha;
    private BigDecimal precioEntrada;
    private EventoTipoDTO eventoTipo;
    private Long id;

    public static class EventoTipoDTO {
        private String nombre;
        private String descripcion;

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }
    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecioEntrada() {
        return precioEntrada;
    }
    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public EventoTipoDTO getEventoTipo() {
        return eventoTipo;
    }
    public void setEventoTipo(EventoTipoDTO eventoTipo) {
        this.eventoTipo = eventoTipo;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
