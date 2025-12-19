package com.mycompany.proxy.service.dto;

import java.time.Instant;
import java.util.List;

public class VentaRequestDTO {

    private Long eventoId;
    private Instant fecha;
    private Double precioVenta;
    private List<AsientoVentaDTO> asientos;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public List<AsientoVentaDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoVentaDTO> asientos) {
        this.asientos = asientos;
    }

    public static class AsientoVentaDTO {
        private int fila;
        private int columna;
        private String persona;

        public AsientoVentaDTO() {}

        public AsientoVentaDTO(int fila, int columna, String persona) {
            this.fila = fila;
            this.columna = columna;
            this.persona = persona;
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
    }
}
