package com.mycompany.myapp.service.dto.catedra;

import java.time.Instant;
import java.util.List;

public class VentaRequestCatedraDTO {

    private Long eventoId;
    private Instant fecha;
    private Double precioVenta;
    private List<AsientoVentaDTO> asientos;

    public static class AsientoVentaDTO {
        private Integer fila;
        private Integer columna;
        private String persona;

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
    }

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
}
