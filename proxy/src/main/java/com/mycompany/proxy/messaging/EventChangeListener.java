package com.mycompany.proxy.messaging;

import com.mycompany.proxy.service.BackendNotificationService;
import com.mycompany.proxy.service.dto.EventChangeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventChangeListener {

    private static final Logger log = LoggerFactory.getLogger(EventChangeListener.class);

    private final BackendNotificationService backendNotificationService;
    private final String eventTopic;

    public EventChangeListener(
        BackendNotificationService backendNotificationService,
        @Value("${proxy.kafka.event-topic}") String eventTopic
    ) {
        this.backendNotificationService = backendNotificationService;
        this.eventTopic = eventTopic;
    }

    @KafkaListener(topics = "#{__listener.getEventTopic()}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(String message) {
        log.info("Mensaje recibido desde Kafka (tópico {}): {}", eventTopic, message);

        // TODO: cuando conozcamos el formato real del mensaje de la cátedra,
        //       parsear JSON para extraer eventId y changeType.
        EventChangeMessage eventChange = new EventChangeMessage();
        eventChange.setRawPayload(message);

        // Intento simple de detectar un eventId si viene como "eventId":123 en el JSON:
        Long eventId = tryExtractEventIdFromJson(message);
        eventChange.setEventId(eventId);
        eventChange.setChangeType("UNKNOWN"); // luego lo mejoramos

        backendNotificationService.notifyBackendEventChange(eventChange);
    }

    public String getEventTopic() {
        return eventTopic;
    }

    /**
     * Intenta extraer eventId de un JSON muy simple.
     * Esto es sólo heurístico para empezar.
     */
    private Long tryExtractEventIdFromJson(String json) {
        try {
            // Ejemplo súper simple: buscar "eventId":123
            String marker = "\"eventId\":";
            int idx = json.indexOf(marker);
            if (idx == -1) {
                return null;
            }
            int start = idx + marker.length();
            int end = start;
            while (end < json.length() && Character.isDigit(json.charAt(end))) {
                end++;
            }
            String numberStr = json.substring(start, end).trim();
            if (!numberStr.isEmpty()) {
                return Long.parseLong(numberStr);
            }
        } catch (Exception e) {
            log.warn("No se pudo extraer eventId del mensaje JSON: {}", e.getMessage());
        }
        return null;
    }
}
