package com.custom.mall.web.rest;

import static com.custom.mall.domain.ShopUserAsserts.*;
import static com.custom.mall.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.custom.mall.IntegrationTest;
import com.custom.mall.domain.ShopUser;
import com.custom.mall.repository.ShopUserRepository;
import com.custom.mall.repository.search.ShopUserSearchRepository;
import com.custom.mall.service.dto.ShopUserDTO;
import com.custom.mall.service.mapper.ShopUserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ShopUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShopUserResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/shop-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/shop-users/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopUserRepository shopUserRepository;

    @Autowired
    private ShopUserMapper shopUserMapper;

    @Autowired
    private ShopUserSearchRepository shopUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShopUserMockMvc;

    private ShopUser shopUser;

    private ShopUser insertedShopUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopUser createEntity() {
        return new ShopUser().username(DEFAULT_USERNAME).email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShopUser createUpdatedEntity() {
        return new ShopUser().username(UPDATED_USERNAME).email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        shopUser = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedShopUser != null) {
            shopUserRepository.delete(insertedShopUser);
            shopUserSearchRepository.delete(insertedShopUser);
            insertedShopUser = null;
        }
    }

    @Test
    @Transactional
    void createShopUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);
        var returnedShopUserDTO = om.readValue(
            restShopUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopUserDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ShopUserDTO.class
        );

        // Validate the ShopUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedShopUser = shopUserMapper.toEntity(returnedShopUserDTO);
        assertShopUserUpdatableFieldsEquals(returnedShopUser, getPersistedShopUser(returnedShopUser));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedShopUser = returnedShopUser;
    }

    @Test
    @Transactional
    void createShopUserWithExistingId() throws Exception {
        // Create the ShopUser with an existing ID
        shopUser.setId(1L);
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restShopUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        // set the field null
        shopUser.setUsername(null);

        // Create the ShopUser, which fails.
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        restShopUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        // set the field null
        shopUser.setEmail(null);

        // Create the ShopUser, which fails.
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        restShopUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopUserDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllShopUsers() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);

        // Get all the shopUserList
        restShopUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getShopUser() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);

        // Get the shopUser
        restShopUserMockMvc
            .perform(get(ENTITY_API_URL_ID, shopUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shopUser.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getNonExistingShopUser() throws Exception {
        // Get the shopUser
        restShopUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingShopUser() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        shopUserSearchRepository.save(shopUser);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());

        // Update the shopUser
        ShopUser updatedShopUser = shopUserRepository.findById(shopUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedShopUser are not directly saved in db
        em.detach(updatedShopUser);
        updatedShopUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL);
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(updatedShopUser);

        restShopUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shopUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shopUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedShopUserToMatchAllProperties(updatedShopUser);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ShopUser> shopUserSearchList = Streamable.of(shopUserSearchRepository.findAll()).toList();
                ShopUser testShopUserSearch = shopUserSearchList.get(searchDatabaseSizeAfter - 1);

                assertShopUserAllPropertiesEquals(testShopUserSearch, updatedShopUser);
            });
    }

    @Test
    @Transactional
    void putNonExistingShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, shopUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shopUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(shopUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(shopUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateShopUserWithPatch() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shopUser using partial update
        ShopUser partialUpdatedShopUser = new ShopUser();
        partialUpdatedShopUser.setId(shopUser.getId());

        partialUpdatedShopUser.email(UPDATED_EMAIL);

        restShopUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShopUser))
            )
            .andExpect(status().isOk());

        // Validate the ShopUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShopUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedShopUser, shopUser), getPersistedShopUser(shopUser));
    }

    @Test
    @Transactional
    void fullUpdateShopUserWithPatch() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the shopUser using partial update
        ShopUser partialUpdatedShopUser = new ShopUser();
        partialUpdatedShopUser.setId(shopUser.getId());

        partialUpdatedShopUser.username(UPDATED_USERNAME).email(UPDATED_EMAIL);

        restShopUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShopUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedShopUser))
            )
            .andExpect(status().isOk());

        // Validate the ShopUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertShopUserUpdatableFieldsEquals(partialUpdatedShopUser, getPersistedShopUser(partialUpdatedShopUser));
    }

    @Test
    @Transactional
    void patchNonExistingShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, shopUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shopUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(shopUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShopUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        shopUser.setId(longCount.incrementAndGet());

        // Create the ShopUser
        ShopUserDTO shopUserDTO = shopUserMapper.toDto(shopUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShopUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(shopUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShopUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteShopUser() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);
        shopUserRepository.save(shopUser);
        shopUserSearchRepository.save(shopUser);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the shopUser
        restShopUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, shopUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(shopUserSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchShopUser() throws Exception {
        // Initialize the database
        insertedShopUser = shopUserRepository.saveAndFlush(shopUser);
        shopUserSearchRepository.save(shopUser);

        // Search the shopUser
        restShopUserMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + shopUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shopUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    protected long getRepositoryCount() {
        return shopUserRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ShopUser getPersistedShopUser(ShopUser shopUser) {
        return shopUserRepository.findById(shopUser.getId()).orElseThrow();
    }

    protected void assertPersistedShopUserToMatchAllProperties(ShopUser expectedShopUser) {
        assertShopUserAllPropertiesEquals(expectedShopUser, getPersistedShopUser(expectedShopUser));
    }

    protected void assertPersistedShopUserToMatchUpdatableProperties(ShopUser expectedShopUser) {
        assertShopUserAllUpdatablePropertiesEquals(expectedShopUser, getPersistedShopUser(expectedShopUser));
    }
}
