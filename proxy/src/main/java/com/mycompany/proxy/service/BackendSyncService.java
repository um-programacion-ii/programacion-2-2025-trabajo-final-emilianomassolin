package com.mycompany.proxy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BackendSyncService {

    private static final Logger log = LoggerFactory.getLogger(BackendSyncService.class);

    private final RestTemplate restTemplate;
    private final String backendBaseUrl;
    private final String syncPath;
    private final String authToken;

    public BackendSyncService(
        RestTemplate restTemplate,
        @Value("${proxy.backend.base-url}") String backendBaseUrl,
        @Value("${proxy.backend.sync-events-path}") String syncPath,
        @Value("${proxy.backend.auth-token:}") String authToken
    ) {
        this.restTemplate = restTemplate;
        this.backendBaseUrl = backendBaseUrl;
        this.syncPath = syncPath;
        this.authToken = authToken;
    }

    public void syncEventsWithBackend() {
        String url = backendBaseUrl + syncPath;
        log.info("🔄 Notificando a backend para sincronizar eventos: {}", url);

        HttpHeaders headers = new HttpHeaders();
        if (authToken != null && !authToken.isBlank()) {
            headers.setBearerAuth(authToken);
        }
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            log.info("✅ Sync eventos en backend OK. status={}, body={}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            log.error("❌ Error llamando al backend para sync eventos", e);
        }
    }
}
