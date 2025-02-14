package com.custom.mall.web.rest;

import com.custom.mall.repository.FlashSaleRepository;
import com.custom.mall.service.FlashSaleService;
import com.custom.mall.service.dto.FlashSaleDTO;
import com.custom.mall.web.rest.errors.BadRequestAlertException;
import com.custom.mall.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.custom.mall.domain.FlashSale}.
 */
@RestController
@RequestMapping("/api/flash-sales")
public class FlashSaleResource {

    private static final Logger LOG = LoggerFactory.getLogger(FlashSaleResource.class);

    private static final String ENTITY_NAME = "flashSale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlashSaleService flashSaleService;

    private final FlashSaleRepository flashSaleRepository;

    public FlashSaleResource(FlashSaleService flashSaleService, FlashSaleRepository flashSaleRepository) {
        this.flashSaleService = flashSaleService;
        this.flashSaleRepository = flashSaleRepository;
    }

    /**
     * {@code POST  /flash-sales} : Create a new flashSale.
     *
     * @param flashSaleDTO the flashSaleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flashSaleDTO, or with status {@code 400 (Bad Request)} if the flashSale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FlashSaleDTO> createFlashSale(@Valid @RequestBody FlashSaleDTO flashSaleDTO) throws URISyntaxException {
        LOG.debug("REST request to save FlashSale : {}", flashSaleDTO);
        if (flashSaleDTO.getId() != null) {
            throw new BadRequestAlertException("A new flashSale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        flashSaleDTO = flashSaleService.save(flashSaleDTO);
        return ResponseEntity.created(new URI("/api/flash-sales/" + flashSaleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, flashSaleDTO.getId().toString()))
            .body(flashSaleDTO);
    }

    /**
     * {@code PUT  /flash-sales/:id} : Updates an existing flashSale.
     *
     * @param id the id of the flashSaleDTO to save.
     * @param flashSaleDTO the flashSaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flashSaleDTO,
     * or with status {@code 400 (Bad Request)} if the flashSaleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flashSaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FlashSaleDTO> updateFlashSale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FlashSaleDTO flashSaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update FlashSale : {}, {}", id, flashSaleDTO);
        if (flashSaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flashSaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flashSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        flashSaleDTO = flashSaleService.update(flashSaleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flashSaleDTO.getId().toString()))
            .body(flashSaleDTO);
    }

    /**
     * {@code PATCH  /flash-sales/:id} : Partial updates given fields of an existing flashSale, field will ignore if it is null
     *
     * @param id the id of the flashSaleDTO to save.
     * @param flashSaleDTO the flashSaleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flashSaleDTO,
     * or with status {@code 400 (Bad Request)} if the flashSaleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the flashSaleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the flashSaleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FlashSaleDTO> partialUpdateFlashSale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FlashSaleDTO flashSaleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update FlashSale partially : {}, {}", id, flashSaleDTO);
        if (flashSaleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flashSaleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flashSaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FlashSaleDTO> result = flashSaleService.partialUpdate(flashSaleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flashSaleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /flash-sales} : get all the flashSales.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flashSales in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FlashSaleDTO>> getAllFlashSales(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of FlashSales");
        Page<FlashSaleDTO> page = flashSaleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flash-sales/:id} : get the "id" flashSale.
     *
     * @param id the id of the flashSaleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flashSaleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlashSaleDTO> getFlashSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to get FlashSale : {}", id);
        Optional<FlashSaleDTO> flashSaleDTO = flashSaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flashSaleDTO);
    }

    /**
     * {@code DELETE  /flash-sales/:id} : delete the "id" flashSale.
     *
     * @param id the id of the flashSaleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashSale(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete FlashSale : {}", id);
        flashSaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /flash-sales/_search?query=:query} : search for the flashSale corresponding
     * to the query.
     *
     * @param query the query of the flashSale search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<FlashSaleDTO>> searchFlashSales(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of FlashSales for query {}", query);
        try {
            Page<FlashSaleDTO> page = flashSaleService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
