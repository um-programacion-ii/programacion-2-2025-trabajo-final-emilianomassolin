package com.mycompany.myapp.service.dto.proxy;

public class SeatDTO {

    private int fila;
    private int columna;
    private SeatStatus estado;

    public SeatDTO() {
    }

    public SeatDTO(int fila, int columna, SeatStatus estado) {
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

    public SeatStatus getEstado() {
        return estado;
    }

    public void setEstado(SeatStatus estado) {
        this.estado = estado;
    }
}
