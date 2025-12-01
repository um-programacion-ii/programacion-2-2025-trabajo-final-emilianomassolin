package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CatedraService;
import com.mycompany.myapp.service.dto.catedra.EventoResumidoDTO;
import com.mycompany.myapp.service.dto.catedra.EventoCompletoDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints mobile para eventos.
 *
 * /api/mobile/eventos        -> lista resumida
 * /api/mobile/eventos/{id}   -> detalle completo
 */
@RestController
@RequestMapping("/api/mobile")
public class MobileEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(MobileEventResource.class);

    private final CatedraService catedraService;

    public MobileEventResource(CatedraService catedraService) {
        this.catedraService = catedraService;
    }

    /**
     * GET  /eventos : devuelve lista resumida de eventos para el mobile.
     */
    @GetMapping("/eventos")
    public List<EventoResumidoDTO> listarEventosResumidos() {
        LOG.debug("API mobile: listar eventos resumidos");
        return catedraService.obtenerEventosResumidos();
    }

    /**
     * GET  /eventos/{id} : devuelve datos completos de un evento.
     */
    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoCompletoDTO> obtenerEvento(@PathVariable Long id) {
        LOG.debug("API mobile: obtener evento {}", id);
        EventoCompletoDTO evento = catedraService.obtenerEventoPorId(id);
        if (evento == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(evento);
    }
}
