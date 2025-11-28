package com.mycompany.myapp.service.dto;

import java.time.Instant;
import java.util.List;

public class EventoCatedraDTO {

    public String titulo;
    public String resumen;
    public String descripcion;
    public Instant fecha;
    public String direccion;
    public String imagen;
    public Integer filaAsientos;
    public Integer columnAsientos;
    public Double precioEntrada;
    public EventoTipoDTO eventoTipo;
    public List<IntegranteDTO> integrantes;
    public Long id; // id del evento en la cátedra

    public static class EventoTipoDTO {
        public String nombre;
        public String descripcion;
    }

    public static class IntegranteDTO {
        public String nombre;
        public String apellido;
        public String identificacion;
    }
}
