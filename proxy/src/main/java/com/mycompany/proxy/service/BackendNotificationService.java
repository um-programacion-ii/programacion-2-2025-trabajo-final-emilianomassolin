package com.mycompany.proxy.service;

import com.mycompany.proxy.service.dto.EventChangeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class BackendNotificationService {

    private static final Logger log = LoggerFactory.getLogger(BackendNotificationService.class);

    private final RestTemplate restTemplate;
    private final String backendBaseUrl;
    private final String backendSyncPath;
    private final String backendAuthToken;

    public BackendNotificationService(
        RestTemplate restTemplate,
        @Value("${proxy.backend.base-url}") String backendBaseUrl,
        @Value("${proxy.backend.sync-events-path}") String backendSyncPath,
        @Value("${proxy.backend.auth-token:}") String backendAuthToken
    ) {
        this.restTemplate = restTemplate;
        this.backendBaseUrl = backendBaseUrl;
        this.backendSyncPath = backendSyncPath;
        this.backendAuthToken = backendAuthToken;
    }

    /**
     * Notifica al backend que hubo un cambio de evento.
     * Estrategia simple:
     *   - llama al endpoint de sincronización completa de eventos.
     *   - más adelante se podría hacer algo más fino por eventId.
     */
    public void notifyBackendEventChange(EventChangeMessage msg) {
        try {
            String url = UriComponentsBuilder
                .fromHttpUrl(backendBaseUrl)
                .path(backendSyncPath)
                .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (backendAuthToken != null && !backendAuthToken.isBlank()) {
                headers.setBearerAuth(backendAuthToken);
            }

            // Si tu endpoint POST /sync-from-catedra no espera body, mandamos vacío.
            HttpEntity<Void> request = new HttpEntity<>(headers);

            log.info("Notificando al backend cambio de evento {} (tipo: {}). URL: {}",
                msg.getEventId(), msg.getChangeType(), url);

            ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            log.info("Respuesta del backend a la notificación de cambio de evento: {} {}",
                response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            log.error("Error notificando al backend sobre cambio de evento: {}", e.getMessage(), e);
        }
    }
}
