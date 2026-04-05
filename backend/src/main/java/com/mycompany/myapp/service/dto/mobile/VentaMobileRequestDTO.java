package com.mycompany.myapp.service.dto.mobile;

import java.time.Instant;
import java.util.List;

public class VentaMobileRequestDTO {

    private Long eventoId;
    private Instant fecha;
    private Double precioVenta;
    private List<String> personas;

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

    public List<String> getPersonas() {
        return personas;
    }

    public void setPersonas(List<String> personas) {
        this.personas = personas;
    }

    @Override
    public String toString() {
        return "VentaMobileRequestDTO{" +
            "eventoId=" + eventoId +
            ", fecha=" + fecha +
            ", precioVenta=" + precioVenta +
            ", personas=" + personas +
            '}';
    }
}
