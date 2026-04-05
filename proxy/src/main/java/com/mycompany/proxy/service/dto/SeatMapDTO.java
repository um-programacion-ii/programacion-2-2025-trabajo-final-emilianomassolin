package com.mycompany.proxy.service.dto;

import java.util.List;

public class SeatMapDTO {

    private Long eventoId;
    private int filas;
    private int columnas;
    private List<SeatDTO> asientos;

    public SeatMapDTO() {
    }

    public SeatMapDTO(Long eventoId, int filas, int columnas, List<SeatDTO> asientos) {
        this.eventoId = eventoId;
        this.filas = filas;
        this.columnas = columnas;
        this.asientos = asientos;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public List<SeatDTO> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<SeatDTO> asientos) {
        this.asientos = asientos;
    }
}
