package com.mycompany.proxy.service;

import com.mycompany.proxy.service.dto.BloqueoRequestDTO;
import com.mycompany.proxy.service.dto.BloqueoResponseDTO;
import com.mycompany.proxy.service.dto.VentaRequestDTO;
import com.mycompany.proxy.service.dto.VentaResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatedraSeatService {

    private static final Logger log = LoggerFactory.getLogger(CatedraSeatService.class);

    private final RestTemplate restTemplate;
    private final String catedraBaseUrl;
    private final String catedraToken;

    public CatedraSeatService(
        RestTemplate restTemplate,
        @Value("${proxy.catedra.base-url}") String catedraBaseUrl,
        @Value("${proxy.catedra.token}") String catedraToken
    ) {
        this.restTemplate = restTemplate;
        this.catedraBaseUrl = catedraBaseUrl;
        this.catedraToken = catedraToken;
    }

    public BloqueoResponseDTO bloquearAsientos(BloqueoRequestDTO request) {
        String url = catedraBaseUrl + "/api/endpoints/v1/bloquear-asientos";
        log.debug("Llamando a cátedra para BLOQUEAR asientos. URL={}, body={}", url, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(catedraToken);

        HttpEntity<BloqueoRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<BloqueoResponseDTO> response =
            restTemplate.exchange(url, HttpMethod.POST, entity, BloqueoResponseDTO.class);

        return response.getBody();
    }

    public VentaResponseDTO realizarVenta(VentaRequestDTO request) {
        String url = catedraBaseUrl + "/api/endpoints/v1/realizar-venta";
        log.debug("Llamando a cátedra para REALIZAR VENTA. URL={}, body={}", url, request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(catedraToken);

        HttpEntity<VentaRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<VentaResponseDTO> response =
            restTemplate.exchange(url, HttpMethod.POST, entity, VentaResponseDTO.class);

        return response.getBody();
    }
}
