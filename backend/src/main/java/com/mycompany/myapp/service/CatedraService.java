package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.catedra.EventoResumidoDTO;
import com.mycompany.myapp.service.dto.catedra.EventoCompletoDTO;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.mycompany.myapp.service.dto.catedra.VentaRequestCatedraDTO;
import com.mycompany.myapp.service.dto.catedra.VentaResponseCatedraDTO;
import com.mycompany.myapp.service.dto.catedra.BloqueoAsientosRequestDTO;
import com.mycompany.myapp.service.dto.catedra.BloqueoAsientosResponseDTO;



@Service
public class CatedraService {

    private final Logger log = LoggerFactory.getLogger(CatedraService.class);

    private final RestTemplate catedraRestTemplate;

    public CatedraService(RestTemplate catedraRestTemplate) {
        this.catedraRestTemplate = catedraRestTemplate;
    }

    public List<EventoResumidoDTO> obtenerEventosResumidos() {
        log.debug("Llamando a cátedra: /api/endpoints/v1/eventos-resumidos");
        EventoResumidoDTO[] array = catedraRestTemplate.getForObject(
            "/api/endpoints/v1/eventos-resumidos",
            EventoResumidoDTO[].class
        );
        if (array == null) {
            return List.of();
        }
        return Arrays.asList(array);
    }

    public List<EventoCompletoDTO> obtenerEventosCompletos() {
        log.debug("Llamando a cátedra: /api/endpoints/v1/eventos");
        EventoCompletoDTO[] array = catedraRestTemplate.getForObject(
            "/api/endpoints/v1/eventos",
            EventoCompletoDTO[].class
        );
        if (array == null) {
            return List.of();
        }
        return Arrays.asList(array);
    }

    public EventoCompletoDTO obtenerEventoPorId(Long id) {
        log.debug("Llamando a cátedra: /api/endpoints/v1/evento/{}", id);
        return catedraRestTemplate.getForObject(
            "/api/endpoints/v1/evento/{id}",
            EventoCompletoDTO.class,
            id
        );
    }
    public VentaResponseCatedraDTO realizarVenta(VentaRequestCatedraDTO request) {
        log.debug("Llamando a cátedra: /api/endpoints/v1/realizar-venta, request={}", request);
        return catedraRestTemplate.postForObject(
            "/api/endpoints/v1/realizar-venta",
            request,
            VentaResponseCatedraDTO.class
        );
    }
    public BloqueoAsientosResponseDTO bloquearAsientos(BloqueoAsientosRequestDTO request) {
        log.debug("Llamando a cátedra: /api/endpoints/v1/bloquear-asientos, request={}", request);
        return catedraRestTemplate.postForObject(
            "/api/endpoints/v1/bloquear-asientos",
            request,
            BloqueoAsientosResponseDTO.class
        );
    }


}
