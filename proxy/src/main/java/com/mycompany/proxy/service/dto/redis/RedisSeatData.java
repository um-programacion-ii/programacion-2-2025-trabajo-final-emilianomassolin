package com.mycompany.proxy.service.dto.redis;

import java.util.List;

public class RedisSeatData {

    private Long eventoId;
    private List<RedisSeat> asientos;

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public List<RedisSeat> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<RedisSeat> asientos) {
        this.asientos = asientos;
    }

    public static class RedisSeat {
        private int fila;
        private int columna;
        private String estado;
        private String expira; // puede venir null

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

        public String getExpira() {
            return expira;
        }

        public void setExpira(String expira) {
            this.expira = expira;
        }
    }
}
