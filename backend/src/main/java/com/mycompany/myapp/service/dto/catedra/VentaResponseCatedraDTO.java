package com.mycompany.myapp.service.dto.catedra;

import java.time.Instant;
import java.util.List;

public class VentaResponseCatedraDTO {

    private Long eventoId;
    private Long ventaId;
    private Instant fechaVenta;
    private List<AsientoResponseDTO> asientos;
    private Boolean resultado;
    private String descripcion;
    private Double precioVenta;

    public static class AsientoResponseDTO {
        private Integer fila;
        private Integer columna;
        private String persona;
        private String estado;

        public Integer getFila() {
            return fila;
        }
        public void setFila(Integer fila) {
            this.fila = fila;
        }

        public Integer getColumna() {
            return columna;
        }
        public void setColumna(Integer columna) {
            this.columna = columna;
        }

        public String getPersona() {
            return persona;
        }
        public void setPersona(String persona) {
            this.persona = persona;
        }

        public String getEstado() {
            return estado;
        }
        public void setEstado(String estado) {
            this.estado = estado;
        }
    }

    public Long getEventoId() {
        return eventoId;
    }
    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getVentaId() {
        return ventaId;
    }
    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }
    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<AsientoResponseDTO> getAsientos() {
        return asientos;
    }
    public void setAsientos(List<AsientoResponseDTO> asientos) {
        this.asientos = asientos;
    }

    public Boolean getResultado() {
        return resultado;
    }
    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }
}
