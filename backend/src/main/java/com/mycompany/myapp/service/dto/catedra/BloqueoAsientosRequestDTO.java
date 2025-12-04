package com.mycompany.myapp.service.dto.catedra;

import java.util.List;

public class BloqueoAsientosRequestDTO {

    private Long eventoId;
    private List<AsientoDTO> asientos;

    public static class AsientoDTO {
        private Integer fila;
        private Integer columna;

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

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<AsientoDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoDTO> asientos) {
        this.asientos = asientos;
    }
}
