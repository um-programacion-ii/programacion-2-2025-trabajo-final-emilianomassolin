package com.mycompany.proxy.web.rest;

import com.mycompany.proxy.service.CatedraSeatService;
import com.mycompany.proxy.service.dto.BloqueoRequestDTO;
import com.mycompany.proxy.service.dto.BloqueoResponseDTO;
import com.mycompany.proxy.service.dto.VentaRequestDTO;
import com.mycompany.proxy.service.dto.VentaResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proxy")
public class SeatActionResource {

    private static final Logger log = LoggerFactory.getLogger(SeatActionResource.class);

    private final CatedraSeatService catedraSeatService;

    public SeatActionResource(CatedraSeatService catedraSeatService) {
        this.catedraSeatService = catedraSeatService;
    }

    /**
     * POST /api/proxy/eventos/bloquear-asientos
     * Body: BloqueoRequestDTO (eventoId + lista de asientos)
     */
    @PostMapping("/eventos/bloquear-asientos")
    public ResponseEntity<BloqueoResponseDTO> bloquearAsientos(@RequestBody BloqueoRequestDTO request) {
        log.debug("Proxy: bloquear asientos contra cátedra. request={}", request);
        BloqueoResponseDTO resp = catedraSeatService.bloquearAsientos(request);
        return ResponseEntity.ok(resp);
    }

    /**
     * POST /api/proxy/eventos/realizar-venta
     * Body: VentaRequestDTO (eventoId, fecha, precioVenta, asientos con persona)
     */
    @PostMapping("/eventos/realizar-venta")
    public ResponseEntity<VentaResponseDTO> realizarVenta(@RequestBody VentaRequestDTO request) {
        log.debug("Proxy: realizar venta contra cátedra. request={}", request);
        VentaResponseDTO resp = catedraSeatService.realizarVenta(request);
        return ResponseEntity.ok(resp);
    }
}
