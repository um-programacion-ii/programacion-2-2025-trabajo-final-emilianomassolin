package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Sale;
import com.mycompany.myapp.repository.SaleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mycompany.myapp.service.dto.catedra.VentaRequestCatedraDTO;
import com.mycompany.myapp.service.dto.catedra.VentaResponseCatedraDTO;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Sale}.
 */
@Service
@Transactional
public class SaleService {

    private static final Logger LOG = LoggerFactory.getLogger(SaleService.class);
    private final CatedraService catedraService;


    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository, CatedraService catedraService) {
        this.saleRepository = saleRepository;
        this.catedraService = catedraService;
    }


    /**
     * Save a sale.
     *
     * @param sale the entity to save.
     * @return the persisted entity.
     */
    public Sale save(Sale sale) {
        LOG.debug("Request to save Sale : {}", sale);
        return saleRepository.save(sale);
    }

    /**
     * Update a sale.
     *
     * @param sale the entity to save.
     * @return the persisted entity.
     */
    public Sale update(Sale sale) {
        LOG.debug("Request to update Sale : {}", sale);
        return saleRepository.save(sale);
    }

    /**
     * Partially update a sale.
     *
     * @param sale the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Sale> partialUpdate(Sale sale) {
        LOG.debug("Request to partially update Sale : {}", sale);

        return saleRepository
            .findById(sale.getId())
            .map(existingSale -> {
                if (sale.getVentaId() != null) {
                    existingSale.setVentaId(sale.getVentaId());
                }
                if (sale.getEventId() != null) {
                    existingSale.setEventId(sale.getEventId());
                }
                if (sale.getFechaVenta() != null) {
                    existingSale.setFechaVenta(sale.getFechaVenta());
                }
                if (sale.getAsientos() != null) {
                    existingSale.setAsientos(sale.getAsientos());
                }
                if (sale.getNombres() != null) {
                    existingSale.setNombres(sale.getNombres());
                }
                if (sale.getPrecioVenta() != null) {
                    existingSale.setPrecioVenta(sale.getPrecioVenta());
                }
                if (sale.getEstado() != null) {
                    existingSale.setEstado(sale.getEstado());
                }
                if (sale.getResultado() != null) {
                    existingSale.setResultado(sale.getResultado());
                }
                if (sale.getDescripcion() != null) {
                    existingSale.setDescripcion(sale.getDescripcion());
                }
                if (sale.getCantidadAsientos() != null) {
                    existingSale.setCantidadAsientos(sale.getCantidadAsientos());
                }

                return existingSale;
            })
            .map(saleRepository::save);
    }

    /**
     * Get all the sales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Sale> findAll(Pageable pageable) {
        LOG.debug("Request to get all Sales");
        return saleRepository.findAll(pageable);
    }

    /**
     * Get one sale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Sale> findOne(Long id) {
        LOG.debug("Request to get Sale : {}", id);
        return saleRepository.findById(id);
    }

    /**
     * Delete the sale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sale : {}", id);
        saleRepository.deleteById(id);
    }
    /**
     * Realiza una venta contra el servicio de la cátedra.
     * - Si la llamada es exitosa, guarda la venta como EXITOSA o FALLIDA según la respuesta.
     * - Si hay error de conexión u otra excepción, registra la venta como PENDIENTE.
     *
     * @param request  los datos que se envían a la cátedra.
     * @param username usuario que realiza la venta (por ahora solo se loguea).
     * @return la entidad Sale guardada.
     */
    public Sale realizarVentaContraCatedra(VentaRequestCatedraDTO request, String username) {
        LOG.debug("Request to realizar venta contra cátedra. Evento={}, usuario={}", request.getEventoId(), username);

        try {
            // 1) Llamamos a la cátedra
            VentaResponseCatedraDTO respuesta = catedraService.realizarVenta(request);
            LOG.debug("Respuesta de cátedra para venta: {}", respuesta);

            // 2) Armamos el Sale local a partir de la respuesta
            Sale sale = new Sale();
            sale.setVentaId(respuesta.getVentaId());              // ventaId que devuelve la cátedra
            sale.setEventId(respuesta.getEventoId());             // eventoId de la cátedra
            sale.setFechaVenta(
                respuesta.getFechaVenta() != null ? respuesta.getFechaVenta() : Instant.now()
            );
            sale.setPrecioVenta(respuesta.getPrecioVenta());      // precioVenta respuesta

            sale.setResultado(respuesta.getResultado());          // true / false
            sale.setDescripcion(respuesta.getDescripcion());      // mensaje "Venta realizada..." o "Venta rechazada..."

            // estado interno nuestro
            sale.setEstado(Boolean.TRUE.equals(respuesta.getResultado()) ? "EXITOSA" : "FALLIDA");

            // cantidad de asientos + strings de asientos/nombres
            if (respuesta.getAsientos() != null && !respuesta.getAsientos().isEmpty()) {
                sale.setCantidadAsientos(respuesta.getAsientos().size());

                String asientosStr = respuesta.getAsientos().stream()
                    .map(a -> "(" + a.getFila() + "," + a.getColumna() + ")")
                    .collect(Collectors.joining(","));
                sale.setAsientos(asientosStr);

                String nombresStr = respuesta.getAsientos().stream()
                    .map(a -> a.getPersona() != null ? a.getPersona() : "")
                    .collect(Collectors.joining(","));
                sale.setNombres(nombresStr);
            } else {
                sale.setCantidadAsientos(0);
                sale.setAsientos("");
                sale.setNombres("");
            }

            LOG.debug("Guardando Sale local a partir de respuesta de cátedra: {}", sale);
            return saleRepository.save(sale);

        } catch (Exception e) {
            // 3) Error al conectar con cátedra → venta PENDIENTE
            LOG.error("Error al conectar con cátedra para realizar venta", e);

            Sale sale = new Sale();
            sale.setVentaId(null);
            sale.setEventId(request.getEventoId());
            sale.setFechaVenta(Instant.now());
            sale.setPrecioVenta(request.getPrecioVenta());

            sale.setResultado(false);
            sale.setDescripcion("Venta pendiente. No se pudo contactar al servidor de la cátedra.");
            sale.setEstado("PENDIENTE");

            if (request.getAsientos() != null && !request.getAsientos().isEmpty()) {
                sale.setCantidadAsientos(request.getAsientos().size());

                String asientosStr = request.getAsientos().stream()
                    .map(a -> "(" + a.getFila() + "," + a.getColumna() + ")")
                    .collect(Collectors.joining(","));
                sale.setAsientos(asientosStr);

                String nombresStr = request.getAsientos().stream()
                    .map(a -> a.getPersona() != null ? a.getPersona() : "")
                    .collect(Collectors.joining(","));
                sale.setNombres(nombresStr);
            } else {
                sale.setCantidadAsientos(0);
                sale.setAsientos("");
                sale.setNombres("");
            }

            LOG.debug("Guardando Sale local como PENDIENTE: {}", sale);
            return saleRepository.save(sale);
        }
    }

    /**
     * Reintenta todas las ventas en estado PENDIENTE llamando nuevamente a la cátedra.
     *
     * @param username Usuario técnico que dispara el reintento (solo para logging).
     * @return lista de Sales actualizadas.
     */
    public List<Sale> reintentarVentasPendientes(String username) {
        LOG.debug("Reintentando ventas pendientes. Usuario técnico={}", username);

        List<Sale> pendientes = saleRepository.findByEstado("PENDIENTE");
        List<Sale> actualizadas = new ArrayList<>();

        for (Sale pendiente : pendientes) {
            try {
                LOG.debug("Reintentando venta pendiente id={}, eventId={}", pendiente.getId(), pendiente.getEventId());

                // 1) Reconstruimos VentaRequestCatedraDTO desde la Sale
                VentaRequestCatedraDTO request = new VentaRequestCatedraDTO();
                request.setEventoId(pendiente.getEventId());
                request.setFecha(
                    pendiente.getFechaVenta() != null ? pendiente.getFechaVenta() : Instant.now()
                );
                request.setPrecioVenta(pendiente.getPrecioVenta());

                List<VentaRequestCatedraDTO.AsientoVentaDTO> asientos = new ArrayList<>();

                if (pendiente.getAsientos() != null && !pendiente.getAsientos().isBlank()) {
                    // "(8,1),(8,2)" → ["(8,1)", "(8,2)"]
                    String[] partesAsientos = pendiente.getAsientos().split(",");
                    String[] partesNombres = pendiente.getNombres() != null
                        ? pendiente.getNombres().split(",")
                        : new String[0];

                    for (int i = 0; i < partesAsientos.length; i++) {
                        String p = partesAsientos[i].trim();   // "(8" o "(8,1)" según cómo venga

                        // Sacamos paréntesis
                        p = p.replace("(", "").replace(")", "");
                        String[] filaCol = p.split(",");
                        if (filaCol.length != 2) {
                            continue;
                        }

                        Integer fila = Integer.parseInt(filaCol[0].trim());
                        Integer columna = Integer.parseInt(filaCol[1].trim());
                        String persona = (i < partesNombres.length) ? partesNombres[i].trim() : "";

                        VentaRequestCatedraDTO.AsientoVentaDTO asientoDTO = new VentaRequestCatedraDTO.AsientoVentaDTO();
                        asientoDTO.setFila(fila);
                        asientoDTO.setColumna(columna);
                        asientoDTO.setPersona(persona);
                        asientos.add(asientoDTO);
                    }
                }

                request.setAsientos(asientos);

                // 2) Llamamos a la cátedra otra vez
                VentaResponseCatedraDTO resp = catedraService.realizarVenta(request);
                LOG.debug("Respuesta de cátedra en reintento para venta id={}: {}", pendiente.getId(), resp);

                // 3) Actualizamos la Sale pendiente con la nueva respuesta
                pendiente.setVentaId(resp.getVentaId());
                pendiente.setFechaVenta(
                    resp.getFechaVenta() != null ? resp.getFechaVenta() : Instant.now()
                );
                pendiente.setPrecioVenta(resp.getPrecioVenta());
                pendiente.setResultado(resp.getResultado());
                pendiente.setDescripcion(resp.getDescripcion());

                if (Boolean.TRUE.equals(resp.getResultado())) {
                    pendiente.setEstado("EXITOSA");
                } else {
                    pendiente.setEstado("FALLIDA");
                }

                if (resp.getAsientos() != null && !resp.getAsientos().isEmpty()) {
                    pendiente.setCantidadAsientos(resp.getAsientos().size());

                    String asientosStr = resp.getAsientos().stream()
                        .map(a -> "(" + a.getFila() + "," + a.getColumna() + ")")
                        .collect(Collectors.joining(","));
                    pendiente.setAsientos(asientosStr);

                    String nombresStr = resp.getAsientos().stream()
                        .map(a -> a.getPersona() != null ? a.getPersona() : "")
                        .collect(Collectors.joining(","));
                    pendiente.setNombres(nombresStr);
                }

                Sale guardada = saleRepository.save(pendiente);
                actualizadas.add(guardada);

            } catch (Exception e) {
                LOG.error("Error reintentando venta pendiente id={}", pendiente.getId(), e);
                // Si falla de nuevo, la dejamos en PENDIENTE
            }
        }

        return actualizadas;
    }

}
