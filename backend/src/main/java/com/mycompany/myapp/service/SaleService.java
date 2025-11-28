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
     * Realiza una venta contra el servicio de la cátedra y guarda el resultado como Sale.
     *
     * @param request los datos que se envían a la cátedra.
     * @param username usuario que realiza la venta (por ahora lo podrías guardar dentro de descripcion o nombres si querés).
     * @return la entidad Sale guardada.
     */
    public Sale realizarVentaContraCatedra(VentaRequestCatedraDTO request, String username) {
        LOG.debug("Request to realizar venta contra cátedra. Evento={}, usuario={}", request.getEventoId(), username);

        // 1) Llamamos a la cátedra
        VentaResponseCatedraDTO respuesta = catedraService.realizarVenta(request);

        // 2) Armamos el Sale local
        Sale sale = new Sale();
        sale.setVentaId(respuesta.getVentaId());              // ventaId que devuelve la cátedra
        sale.setEventId(respuesta.getEventoId());             // eventoId de la cátedra
        sale.setFechaVenta(respuesta.getFechaVenta());        // fechaVenta del response
        sale.setPrecioVenta(respuesta.getPrecioVenta());      // precioVenta respuesta

        sale.setResultado(respuesta.getResultado());          // true / false
        sale.setDescripcion(respuesta.getDescripcion());      // mensaje "Venta realizada..." o "Venta rechazada..."

        // estado interno nuestro
        sale.setEstado(respuesta.getResultado() ? "EXITOSA" : "FALLIDA");

        // cantidad de asientos
        int cantidadAsientos = respuesta.getAsientos() != null ? respuesta.getAsientos().size() : 0;
        sale.setCantidadAsientos(cantidadAsientos);

        // Como en tu entidad 'Sale' asientos y nombres son String,
        // por ahora los guardamos como representación textual sencilla:
        if (respuesta.getAsientos() != null) {
            String asientosStr = respuesta.getAsientos().stream()
                .map(a -> "(" + a.getFila() + "," + a.getColumna() + ")")
                .reduce((a, b) -> a + "," + b)
                .orElse("");
            sale.setAsientos(asientosStr);

            String nombresStr = respuesta.getAsientos().stream()
                .map(a -> a.getPersona())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
            sale.setNombres(nombresStr);
        } else {
            sale.setAsientos("");
            sale.setNombres("");
        }

        // Por ahora no tenés un campo username en Sale, pero si querés podrías usar 'descripcion'
        // o agregar un nuevo campo en la entidad para guardar quién compró.

        LOG.debug("Guardando Sale local a partir de respuesta de cátedra: {}", sale);
        return saleRepository.save(sale);
    }

}
