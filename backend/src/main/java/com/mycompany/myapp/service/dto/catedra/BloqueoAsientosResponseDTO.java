package com.mycompany.myapp.service.dto.catedra;

import java.util.List;

public class BloqueoAsientosResponseDTO {

    private Boolean resultado;
    private String descripcion;
    private Long eventoId;
    private List<AsientoRespuestaDTO> asientos;

    public static class AsientoRespuestaDTO {
        private String estado;
        private Integer fila;
        private Integer columna;

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }

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

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<AsientoRespuestaDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoRespuestaDTO> asientos) {
        this.asientos = asientos;
    }
}
