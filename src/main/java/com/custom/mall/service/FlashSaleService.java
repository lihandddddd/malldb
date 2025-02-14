package com.custom.mall.service;

import com.custom.mall.domain.FlashSale;
import com.custom.mall.repository.FlashSaleRepository;
import com.custom.mall.repository.search.FlashSaleSearchRepository;
import com.custom.mall.service.dto.FlashSaleDTO;
import com.custom.mall.service.mapper.FlashSaleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.custom.mall.domain.FlashSale}.
 */
@Service
@Transactional
public class FlashSaleService {

    private static final Logger LOG = LoggerFactory.getLogger(FlashSaleService.class);

    private final FlashSaleRepository flashSaleRepository;

    private final FlashSaleMapper flashSaleMapper;

    private final FlashSaleSearchRepository flashSaleSearchRepository;

    public FlashSaleService(
        FlashSaleRepository flashSaleRepository,
        FlashSaleMapper flashSaleMapper,
        FlashSaleSearchRepository flashSaleSearchRepository
    ) {
        this.flashSaleRepository = flashSaleRepository;
        this.flashSaleMapper = flashSaleMapper;
        this.flashSaleSearchRepository = flashSaleSearchRepository;
    }

    /**
     * Save a flashSale.
     *
     * @param flashSaleDTO the entity to save.
     * @return the persisted entity.
     */
    public FlashSaleDTO save(FlashSaleDTO flashSaleDTO) {
        LOG.debug("Request to save FlashSale : {}", flashSaleDTO);
        FlashSale flashSale = flashSaleMapper.toEntity(flashSaleDTO);
        flashSale = flashSaleRepository.save(flashSale);
        flashSaleSearchRepository.index(flashSale);
        return flashSaleMapper.toDto(flashSale);
    }

    /**
     * Update a flashSale.
     *
     * @param flashSaleDTO the entity to save.
     * @return the persisted entity.
     */
    public FlashSaleDTO update(FlashSaleDTO flashSaleDTO) {
        LOG.debug("Request to update FlashSale : {}", flashSaleDTO);
        FlashSale flashSale = flashSaleMapper.toEntity(flashSaleDTO);
        flashSale = flashSaleRepository.save(flashSale);
        flashSaleSearchRepository.index(flashSale);
        return flashSaleMapper.toDto(flashSale);
    }

    /**
     * Partially update a flashSale.
     *
     * @param flashSaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FlashSaleDTO> partialUpdate(FlashSaleDTO flashSaleDTO) {
        LOG.debug("Request to partially update FlashSale : {}", flashSaleDTO);

        return flashSaleRepository
            .findById(flashSaleDTO.getId())
            .map(existingFlashSale -> {
                flashSaleMapper.partialUpdate(existingFlashSale, flashSaleDTO);

                return existingFlashSale;
            })
            .map(flashSaleRepository::save)
            .map(savedFlashSale -> {
                flashSaleSearchRepository.index(savedFlashSale);
                return savedFlashSale;
            })
            .map(flashSaleMapper::toDto);
    }

    /**
     * Get all the flashSales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FlashSaleDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all FlashSales");
        return flashSaleRepository.findAll(pageable).map(flashSaleMapper::toDto);
    }

    /**
     * Get one flashSale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FlashSaleDTO> findOne(Long id) {
        LOG.debug("Request to get FlashSale : {}", id);
        return flashSaleRepository.findById(id).map(flashSaleMapper::toDto);
    }

    /**
     * Delete the flashSale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete FlashSale : {}", id);
        flashSaleRepository.deleteById(id);
        flashSaleSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the flashSale corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FlashSaleDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of FlashSales for query {}", query);
        return flashSaleSearchRepository.search(query, pageable).map(flashSaleMapper::toDto);
    }
}
