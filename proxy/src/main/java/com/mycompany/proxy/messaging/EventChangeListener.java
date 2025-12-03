package com.mycompany.proxy.messaging;

import com.mycompany.proxy.service.BackendSyncService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventChangeListener {

    private static final Logger log = LoggerFactory.getLogger(EventChangeListener.class);

    private final BackendSyncService backendSyncService;

    public EventChangeListener(BackendSyncService backendSyncService) {
        this.backendSyncService = backendSyncService;
    }

    @KafkaListener(
        topics = "${proxy.kafka.event-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void onEventChange(ConsumerRecord<String, String> record) {
        String topic = record.topic();
        int partition = record.partition();
        long offset = record.offset();
        String key = record.key();
        String value = record.value();

        log.info("📩 Mensaje Kafka recibido. topic={}, partition={}, offset={}, key={}, value={}",
            topic, partition, offset, key, value);

        try {
            // Por ahora hacemos un "sync completo" de eventos en tu backend
            backendSyncService.syncEventsWithBackend();
        } catch (Exception e) {
            log.error("Error al notificar al backend para sincronizar eventos", e);
        }
    }
}
