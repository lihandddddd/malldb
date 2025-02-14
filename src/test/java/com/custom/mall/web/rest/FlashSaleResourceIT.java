package com.custom.mall.web.rest;

import static com.custom.mall.domain.FlashSaleAsserts.*;
import static com.custom.mall.web.rest.TestUtil.createUpdateProxyForBean;
import static com.custom.mall.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.custom.mall.IntegrationTest;
import com.custom.mall.domain.FlashSale;
import com.custom.mall.repository.FlashSaleRepository;
import com.custom.mall.repository.search.FlashSaleSearchRepository;
import com.custom.mall.service.dto.FlashSaleDTO;
import com.custom.mall.service.mapper.FlashSaleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link FlashSaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlashSaleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_DISCOUNT_RATE = 0F;
    private static final Float UPDATED_DISCOUNT_RATE = 1F;

    private static final Integer DEFAULT_MAX_QUANTITY = 1;
    private static final Integer UPDATED_MAX_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/flash-sales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/flash-sales/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FlashSaleRepository flashSaleRepository;

    @Autowired
    private FlashSaleMapper flashSaleMapper;

    @Autowired
    private FlashSaleSearchRepository flashSaleSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlashSaleMockMvc;

    private FlashSale flashSale;

    private FlashSale insertedFlashSale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlashSale createEntity() {
        return new FlashSale()
            .name(DEFAULT_NAME)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .discountRate(DEFAULT_DISCOUNT_RATE)
            .maxQuantity(DEFAULT_MAX_QUANTITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlashSale createUpdatedEntity() {
        return new FlashSale()
            .name(UPDATED_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .maxQuantity(UPDATED_MAX_QUANTITY);
    }

    @BeforeEach
    public void initTest() {
        flashSale = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFlashSale != null) {
            flashSaleRepository.delete(insertedFlashSale);
            flashSaleSearchRepository.delete(insertedFlashSale);
            insertedFlashSale = null;
        }
    }

    @Test
    @Transactional
    void createFlashSale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);
        var returnedFlashSaleDTO = om.readValue(
            restFlashSaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FlashSaleDTO.class
        );

        // Validate the FlashSale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFlashSale = flashSaleMapper.toEntity(returnedFlashSaleDTO);
        assertFlashSaleUpdatableFieldsEquals(returnedFlashSale, getPersistedFlashSale(returnedFlashSale));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedFlashSale = returnedFlashSale;
    }

    @Test
    @Transactional
    void createFlashSaleWithExistingId() throws Exception {
        // Create the FlashSale with an existing ID
        flashSale.setId(1L);
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // set the field null
        flashSale.setName(null);

        // Create the FlashSale, which fails.
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // set the field null
        flashSale.setStartTime(null);

        // Create the FlashSale, which fails.
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // set the field null
        flashSale.setEndTime(null);

        // Create the FlashSale, which fails.
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDiscountRateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // set the field null
        flashSale.setDiscountRate(null);

        // Create the FlashSale, which fails.
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMaxQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        // set the field null
        flashSale.setMaxQuantity(null);

        // Create the FlashSale, which fails.
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        restFlashSaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllFlashSales() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);

        // Get all the flashSaleList
        restFlashSaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flashSale.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQuantity").value(hasItem(DEFAULT_MAX_QUANTITY)));
    }

    @Test
    @Transactional
    void getFlashSale() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);

        // Get the flashSale
        restFlashSaleMockMvc
            .perform(get(ENTITY_API_URL_ID, flashSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flashSale.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.discountRate").value(DEFAULT_DISCOUNT_RATE.doubleValue()))
            .andExpect(jsonPath("$.maxQuantity").value(DEFAULT_MAX_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingFlashSale() throws Exception {
        // Get the flashSale
        restFlashSaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlashSale() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        flashSaleSearchRepository.save(flashSale);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());

        // Update the flashSale
        FlashSale updatedFlashSale = flashSaleRepository.findById(flashSale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFlashSale are not directly saved in db
        em.detach(updatedFlashSale);
        updatedFlashSale
            .name(UPDATED_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .maxQuantity(UPDATED_MAX_QUANTITY);
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(updatedFlashSale);

        restFlashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flashSaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(flashSaleDTO))
            )
            .andExpect(status().isOk());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFlashSaleToMatchAllProperties(updatedFlashSale);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<FlashSale> flashSaleSearchList = Streamable.of(flashSaleSearchRepository.findAll()).toList();
                FlashSale testFlashSaleSearch = flashSaleSearchList.get(searchDatabaseSizeAfter - 1);

                assertFlashSaleAllPropertiesEquals(testFlashSaleSearch, updatedFlashSale);
            });
    }

    @Test
    @Transactional
    void putNonExistingFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flashSaleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(flashSaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(flashSaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateFlashSaleWithPatch() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the flashSale using partial update
        FlashSale partialUpdatedFlashSale = new FlashSale();
        partialUpdatedFlashSale.setId(flashSale.getId());

        partialUpdatedFlashSale.endTime(UPDATED_END_TIME);

        restFlashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlashSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFlashSale))
            )
            .andExpect(status().isOk());

        // Validate the FlashSale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFlashSaleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFlashSale, flashSale),
            getPersistedFlashSale(flashSale)
        );
    }

    @Test
    @Transactional
    void fullUpdateFlashSaleWithPatch() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the flashSale using partial update
        FlashSale partialUpdatedFlashSale = new FlashSale();
        partialUpdatedFlashSale.setId(flashSale.getId());

        partialUpdatedFlashSale
            .name(UPDATED_NAME)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .discountRate(UPDATED_DISCOUNT_RATE)
            .maxQuantity(UPDATED_MAX_QUANTITY);

        restFlashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlashSale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFlashSale))
            )
            .andExpect(status().isOk());

        // Validate the FlashSale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFlashSaleUpdatableFieldsEquals(partialUpdatedFlashSale, getPersistedFlashSale(partialUpdatedFlashSale));
    }

    @Test
    @Transactional
    void patchNonExistingFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flashSaleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(flashSaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(flashSaleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlashSale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        flashSale.setId(longCount.incrementAndGet());

        // Create the FlashSale
        FlashSaleDTO flashSaleDTO = flashSaleMapper.toDto(flashSale);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlashSaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(flashSaleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlashSale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteFlashSale() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);
        flashSaleRepository.save(flashSale);
        flashSaleSearchRepository.save(flashSale);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the flashSale
        restFlashSaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, flashSale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(flashSaleSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchFlashSale() throws Exception {
        // Initialize the database
        insertedFlashSale = flashSaleRepository.saveAndFlush(flashSale);
        flashSaleSearchRepository.save(flashSale);

        // Search the flashSale
        restFlashSaleMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + flashSale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flashSale.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].discountRate").value(hasItem(DEFAULT_DISCOUNT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].maxQuantity").value(hasItem(DEFAULT_MAX_QUANTITY)));
    }

    protected long getRepositoryCount() {
        return flashSaleRepository.count();
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

    protected FlashSale getPersistedFlashSale(FlashSale flashSale) {
        return flashSaleRepository.findById(flashSale.getId()).orElseThrow();
    }

    protected void assertPersistedFlashSaleToMatchAllProperties(FlashSale expectedFlashSale) {
        assertFlashSaleAllPropertiesEquals(expectedFlashSale, getPersistedFlashSale(expectedFlashSale));
    }

    protected void assertPersistedFlashSaleToMatchUpdatableProperties(FlashSale expectedFlashSale) {
        assertFlashSaleAllUpdatablePropertiesEquals(expectedFlashSale, getPersistedFlashSale(expectedFlashSale));
    }
}
