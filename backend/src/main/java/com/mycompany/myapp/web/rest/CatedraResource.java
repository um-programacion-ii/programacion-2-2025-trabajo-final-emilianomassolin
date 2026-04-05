package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CatedraService;
import com.mycompany.myapp.service.dto.catedra.EventoResumidoDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catedra")
public class CatedraResource {

    private final CatedraService catedraService;

    public CatedraResource(CatedraService catedraService) {
        this.catedraService = catedraService;
    }

    @GetMapping("/eventos-resumidos")
    public ResponseEntity<List<EventoResumidoDTO>> getEventosResumidos() {
        List<EventoResumidoDTO> eventos = catedraService.obtenerEventosResumidos();
        return ResponseEntity.ok(eventos);
    }
}
