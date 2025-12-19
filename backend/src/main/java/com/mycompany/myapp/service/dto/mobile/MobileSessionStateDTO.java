package com.mycompany.myapp.service.dto.mobile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MobileSessionStateDTO {

    private Long eventoId;
    private String etapa; // LISTA_EVENTOS / ASIENTOS / DATOS_PERSONAS / VENTA_CONFIRMADA
    private List<AsientoSesionDTO> asientos = new ArrayList<>();
    private Instant expiracion;

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }

    public List<AsientoSesionDTO> getAsientos() { return asientos; }
    public void setAsientos(List<AsientoSesionDTO> asientos) { this.asientos = asientos; }

    public Instant getExpiracion() { return expiracion; }
    public void setExpiracion(Instant expiracion) { this.expiracion = expiracion; }
}
