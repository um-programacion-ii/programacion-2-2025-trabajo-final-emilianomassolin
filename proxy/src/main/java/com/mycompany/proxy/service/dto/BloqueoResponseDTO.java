package com.mycompany.proxy.service.dto;

import java.util.List;

public class BloqueoResponseDTO {

    private boolean resultado;
    private String descripcion;
    private Long eventoId;
    private List<AsientoEstadoDTO> asientos;

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

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
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
        private String estado;

        public AsientoEstadoDTO() {}

        public AsientoEstadoDTO(int fila, int columna, String estado) {
            this.fila = fila;
            this.columna = columna;
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

        public String getEstado() {
            return estado;
        }

        public void setEstado(String estado) {
            this.estado = estado;
        }
    }
}
