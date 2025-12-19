package com.mycompany.myapp.service.mobile;

import java.io.Serializable;

public class MobileSeatSelection implements Serializable {

    private int fila;
    private int columna;
    // Null mientras estamos solo seleccionando asientos,
    // se completa en etapa DATOS_PERSONAS
    private String persona;

    public MobileSeatSelection() {}

    public MobileSeatSelection(int fila, int columna, String persona) {
        this.fila = fila;
        this.columna = columna;
        this.persona = persona;
    }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }

    public String getPersona() { return persona; }
    public void setPersona(String persona) { this.persona = persona; }
}
