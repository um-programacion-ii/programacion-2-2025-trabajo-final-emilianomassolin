package com.mycompany.myapp.service.mobile;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MobileSessionState implements Serializable {

    private String username;
    private Long eventoId;
    private List<MobileSeatSelection> asientos = new ArrayList<>();
    private MobileFlowStage etapa;

    private Instant createdAt;
    private Instant lastUpdatedAt;
    private Instant expiresAt;

    // opcional: para debug / auditoría
    private String lastClientInfo; // user-agent, device, etc.

    public MobileSessionState() {}

    public static MobileSessionState nuevaSesion(String username) {
        MobileSessionState s = new MobileSessionState();
        s.username = username;
        s.etapa = MobileFlowStage.LISTA_EVENTOS;
        Instant now = Instant.now();
        s.createdAt = now;
        s.lastUpdatedAt = now;
        return s;
    }

    // getters y setters

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public List<MobileSeatSelection> getAsientos() { return asientos; }
    public void setAsientos(List<MobileSeatSelection> asientos) { this.asientos = asientos; }

    public MobileFlowStage getEtapa() { return etapa; }
    public void setEtapa(MobileFlowStage etapa) { this.etapa = etapa; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastUpdatedAt() { return lastUpdatedAt; }
    public void setLastUpdatedAt(Instant lastUpdatedAt) { this.lastUpdatedAt = lastUpdatedAt; }

    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }

    public String getLastClientInfo() { return lastClientInfo; }
    public void setLastClientInfo(String lastClientInfo) { this.lastClientInfo = lastClientInfo; }
}
