package com.custom.mall.service;

import com.custom.mall.domain.ShopUser;
import com.custom.mall.repository.ShopUserRepository;
import com.custom.mall.repository.search.ShopUserSearchRepository;
import com.custom.mall.service.dto.ShopUserDTO;
import com.custom.mall.service.mapper.ShopUserMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.custom.mall.domain.ShopUser}.
 */
@Service
@Transactional
public class ShopUserService {

    private static final Logger LOG = LoggerFactory.getLogger(ShopUserService.class);

    private final ShopUserRepository shopUserRepository;

    private final ShopUserMapper shopUserMapper;

    private final ShopUserSearchRepository shopUserSearchRepository;

    public ShopUserService(
        ShopUserRepository shopUserRepository,
        ShopUserMapper shopUserMapper,
        ShopUserSearchRepository shopUserSearchRepository
    ) {
        this.shopUserRepository = shopUserRepository;
        this.shopUserMapper = shopUserMapper;
        this.shopUserSearchRepository = shopUserSearchRepository;
    }

    /**
     * Save a shopUser.
     *
     * @param shopUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ShopUserDTO save(ShopUserDTO shopUserDTO) {
        LOG.debug("Request to save ShopUser : {}", shopUserDTO);
        ShopUser shopUser = shopUserMapper.toEntity(shopUserDTO);
        shopUser = shopUserRepository.save(shopUser);
        shopUserSearchRepository.index(shopUser);
        return shopUserMapper.toDto(shopUser);
    }

    /**
     * Update a shopUser.
     *
     * @param shopUserDTO the entity to save.
     * @return the persisted entity.
     */
    public ShopUserDTO update(ShopUserDTO shopUserDTO) {
        LOG.debug("Request to update ShopUser : {}", shopUserDTO);
        ShopUser shopUser = shopUserMapper.toEntity(shopUserDTO);
        shopUser = shopUserRepository.save(shopUser);
        shopUserSearchRepository.index(shopUser);
        return shopUserMapper.toDto(shopUser);
    }

    /**
     * Partially update a shopUser.
     *
     * @param shopUserDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShopUserDTO> partialUpdate(ShopUserDTO shopUserDTO) {
        LOG.debug("Request to partially update ShopUser : {}", shopUserDTO);

        return shopUserRepository
            .findById(shopUserDTO.getId())
            .map(existingShopUser -> {
                shopUserMapper.partialUpdate(existingShopUser, shopUserDTO);

                return existingShopUser;
            })
            .map(shopUserRepository::save)
            .map(savedShopUser -> {
                shopUserSearchRepository.index(savedShopUser);
                return savedShopUser;
            })
            .map(shopUserMapper::toDto);
    }

    /**
     * Get all the shopUsers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShopUserDTO> findAll() {
        LOG.debug("Request to get all ShopUsers");
        return shopUserRepository.findAll().stream().map(shopUserMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the shopUsers where UserProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShopUserDTO> findAllWhereUserProfileIsNull() {
        LOG.debug("Request to get all shopUsers where UserProfile is null");
        return StreamSupport.stream(shopUserRepository.findAll().spliterator(), false)
            .filter(shopUser -> shopUser.getUserProfile() == null)
            .map(shopUserMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one shopUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShopUserDTO> findOne(Long id) {
        LOG.debug("Request to get ShopUser : {}", id);
        return shopUserRepository.findById(id).map(shopUserMapper::toDto);
    }

    /**
     * Delete the shopUser by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ShopUser : {}", id);
        shopUserRepository.deleteById(id);
        shopUserSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the shopUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShopUserDTO> search(String query) {
        LOG.debug("Request to search ShopUsers for query {}", query);
        try {
            return StreamSupport.stream(shopUserSearchRepository.search(query).spliterator(), false).map(shopUserMapper::toDto).toList();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
