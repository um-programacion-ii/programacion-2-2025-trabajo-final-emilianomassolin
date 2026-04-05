package com.mycompany.myapp.service.dto.mobile;

import java.time.Instant;
import java.util.List;

public class SeatSelectionMobileDTO {

    private Long eventoId;
    private List<AsientoSimpleDTO> asientos;
    private Instant expiracion;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<AsientoSimpleDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<AsientoSimpleDTO> asientos) {
        this.asientos = asientos;
    }

    public Instant getExpiracion() {
        return expiracion;
    }

    public void setExpiracion(Instant expiracion) {
        this.expiracion = expiracion;
    }

    public static class AsientoSimpleDTO {
        private int fila;
        private int columna;

        public AsientoSimpleDTO() {}

        public AsientoSimpleDTO(int fila, int columna) {
            this.fila = fila;
            this.columna = columna;
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
    }
}
