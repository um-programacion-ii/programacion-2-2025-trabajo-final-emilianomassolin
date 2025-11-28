package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.service.SaleService;
import com.mycompany.myapp.service.dto.catedra.VentaRequestCatedraDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile")
public class MobileSaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(MobileSaleResource.class);

    private final SaleService saleService;

    public MobileSaleResource(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/venta")
    public Sale realizarVenta(@RequestBody VentaRequestCatedraDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : "anon";

        LOG.debug("API mobile: realizar venta. Usuario={}, request={}", username, request);
        return saleService.realizarVentaContraCatedra(request, username);
    }
}
