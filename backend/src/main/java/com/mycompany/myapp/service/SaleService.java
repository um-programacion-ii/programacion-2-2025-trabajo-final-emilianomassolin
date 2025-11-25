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

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Sale}.
 */
@Service
@Transactional
public class SaleService {

    private static final Logger LOG = LoggerFactory.getLogger(SaleService.class);

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
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
}
