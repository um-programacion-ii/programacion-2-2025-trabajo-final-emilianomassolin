package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.proxy.SeatMapDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProxySeatService {

    private final Logger log = LoggerFactory.getLogger(ProxySeatService.class);

    private final RestTemplate proxyRestTemplate;
    private final String proxyBaseUrl;

    public ProxySeatService(RestTemplate proxyRestTemplate,
                            @Value("${proxy.base-url}") String proxyBaseUrl) {
        this.proxyRestTemplate = proxyRestTemplate;
        this.proxyBaseUrl = proxyBaseUrl;
    }

    public SeatMapDTO getSeatMapForEvent(Long eventoId) {
        String url = UriComponentsBuilder
            .fromHttpUrl(proxyBaseUrl)
            .path("/api/proxy/eventos/asientos")
            .queryParam("eventoId", eventoId)
            .toUriString();

        log.debug("Llamando al proxy para obtener mapa de asientos. URL: {}", url);

        try {
            return proxyRestTemplate.getForObject(url, SeatMapDTO.class);
        } catch (RestClientException e) {
            log.error("Error llamando al proxy para evento {}: {}", eventoId, e.getMessage(), e);
            // Podés decidir qué hacer: devolver null, tirar excepción custom, etc.
            throw e;
        }
    }
}
