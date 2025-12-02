package com.mycompany.proxy.service.dto;

public class EventChangeMessage {

    private Long eventId;
    private String changeType;
    private String rawPayload;

    public EventChangeMessage() {
    }

    public EventChangeMessage(Long eventId, String changeType, String rawPayload) {
        this.eventId = eventId;
        this.changeType = changeType;
        this.rawPayload = rawPayload;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(String rawPayload) {
        this.rawPayload = rawPayload;
    }
}
