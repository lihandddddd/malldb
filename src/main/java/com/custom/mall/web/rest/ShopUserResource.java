package com.custom.mall.web.rest;

import com.custom.mall.repository.ShopUserRepository;
import com.custom.mall.service.ShopUserService;
import com.custom.mall.service.dto.ShopUserDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.custom.mall.domain.ShopUser}.
 */
@RestController
@RequestMapping("/api/shop-users")
public class ShopUserResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserResource.class);

    private static final String ENTITY_NAME = "shopUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShopUserService shopUserService;

    private final ShopUserRepository shopUserRepository;

    public ShopUserResource(ShopUserService shopUserService, ShopUserRepository shopUserRepository) {
        this.shopUserService = shopUserService;
        this.shopUserRepository = shopUserRepository;
    }

    /**
     * {@code POST  /shop-users} : Create a new shopUser.
     *
     * @param shopUserDTO the shopUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shopUserDTO, or with status {@code 400 (Bad Request)} if the shopUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShopUserDTO> createShopUser(@Valid @RequestBody ShopUserDTO shopUserDTO) throws URISyntaxException {
        LOG.debug("REST request to save ShopUser : {}", shopUserDTO);
        if (shopUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new shopUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shopUserDTO = shopUserService.save(shopUserDTO);
        return ResponseEntity.created(new URI("/api/shop-users/" + shopUserDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shopUserDTO.getId().toString()))
            .body(shopUserDTO);
    }

    /**
     * {@code PUT  /shop-users/:id} : Updates an existing shopUser.
     *
     * @param id the id of the shopUserDTO to save.
     * @param shopUserDTO the shopUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopUserDTO,
     * or with status {@code 400 (Bad Request)} if the shopUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shopUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShopUserDTO> updateShopUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShopUserDTO shopUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShopUser : {}, {}", id, shopUserDTO);
        if (shopUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shopUserDTO = shopUserService.update(shopUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopUserDTO.getId().toString()))
            .body(shopUserDTO);
    }

    /**
     * {@code PATCH  /shop-users/:id} : Partial updates given fields of an existing shopUser, field will ignore if it is null
     *
     * @param id the id of the shopUserDTO to save.
     * @param shopUserDTO the shopUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shopUserDTO,
     * or with status {@code 400 (Bad Request)} if the shopUserDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shopUserDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shopUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShopUserDTO> partialUpdateShopUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShopUserDTO shopUserDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShopUser partially : {}, {}", id, shopUserDTO);
        if (shopUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shopUserDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shopUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShopUserDTO> result = shopUserService.partialUpdate(shopUserDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shopUserDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shop-users} : get all the shopUsers.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shopUsers in body.
     */
    @GetMapping("")
    public List<ShopUserDTO> getAllShopUsers(@RequestParam(name = "filter", required = false) String filter) {
        if ("userprofile-is-null".equals(filter)) {
            LOG.debug("REST request to get all ShopUsers where userProfile is null");
            return shopUserService.findAllWhereUserProfileIsNull();
        }
        LOG.debug("REST request to get all ShopUsers");
        return shopUserService.findAll();
    }

    /**
     * {@code GET  /shop-users/:id} : get the "id" shopUser.
     *
     * @param id the id of the shopUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shopUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShopUserDTO> getShopUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShopUser : {}", id);
        Optional<ShopUserDTO> shopUserDTO = shopUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shopUserDTO);
    }

    /**
     * {@code DELETE  /shop-users/:id} : delete the "id" shopUser.
     *
     * @param id the id of the shopUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShopUser(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShopUser : {}", id);
        shopUserService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /shop-users/_search?query=:query} : search for the shopUser corresponding
     * to the query.
     *
     * @param query the query of the shopUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<ShopUserDTO> searchShopUsers(@RequestParam("query") String query) {
        LOG.debug("REST request to search ShopUsers for query {}", query);
        try {
            return shopUserService.search(query);
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
