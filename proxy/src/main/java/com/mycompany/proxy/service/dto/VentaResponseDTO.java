package com.mycompany.proxy.service.dto;

import java.time.Instant;
import java.util.List;

public class VentaResponseDTO {

    private Long eventoId;
    private Long ventaId;
    private Instant fechaVenta;
    private boolean resultado;
    private String descripcion;
    private Double precioVenta;
    private List<AsientoEstadoDTO> asientos;

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

    public boolean isResultado() {
        return resultado;
    }

    public void setResultado(boolean resultado) {
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

    public List<AsientoEstadoDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoEstadoDTO> asientos) {
        this.asientos = asientos;
    }

    public static class AsientoEstadoDTO {
        private int fila;
        private int columna;
        private String persona;
        private String estado;

        public AsientoEstadoDTO() {}

        public AsientoEstadoDTO(int fila, int columna, String persona, String estado) {
            this.fila = fila;
            this.columna = columna;
            this.persona = persona;
            this.estado = estado;
        }

        public int getFila() {
            return fila;
        }

        public void setFila(int fila) {
            this.fila = fila;
        }

        public int getColumna() {
            return columna;
        }

        public void setColumna(int columna) {
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
}
