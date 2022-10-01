package com.foodjeck.platmanager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.foodjeck.platmanager.IntegrationTest;
import com.foodjeck.platmanager.domain.Plate;
import com.foodjeck.platmanager.repository.PlateRepository;
import com.foodjeck.platmanager.service.dto.PlateDTO;
import com.foodjeck.platmanager.service.mapper.PlateMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link PlateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlateResourceIT {

    private static final String DEFAULT_IDRESTAURANT = "AAAAAAAAAA";
    private static final String UPDATED_IDRESTAURANT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/plates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private MockMvc restPlateMockMvc;

    private Plate plate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plate createEntity() {
        Plate plate = new Plate()
            .idrestaurant(DEFAULT_IDRESTAURANT)
            .name(DEFAULT_NAME)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .price(DEFAULT_PRICE);
        return plate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plate createUpdatedEntity() {
        Plate plate = new Plate()
            .idrestaurant(UPDATED_IDRESTAURANT)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .price(UPDATED_PRICE);
        return plate;
    }

    @BeforeEach
    public void initTest() {
        plateRepository.deleteAll();
        plate = createEntity();
    }

    @Test
    void createPlate() throws Exception {
        int databaseSizeBeforeCreate = plateRepository.findAll().size();
        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);
        restPlateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plateDTO)))
            .andExpect(status().isCreated());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeCreate + 1);
        Plate testPlate = plateList.get(plateList.size() - 1);
        assertThat(testPlate.getIdrestaurant()).isEqualTo(DEFAULT_IDRESTAURANT);
        assertThat(testPlate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlate.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPlate.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testPlate.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    void createPlateWithExistingId() throws Exception {
        // Create the Plate with an existing ID
        plate.setId("existing_id");
        PlateDTO plateDTO = plateMapper.toDto(plate);

        int databaseSizeBeforeCreate = plateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPlates() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        // Get all the plateList
        restPlateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plate.getId())))
            .andExpect(jsonPath("$.[*].idrestaurant").value(hasItem(DEFAULT_IDRESTAURANT)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    void getPlate() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        // Get the plate
        restPlateMockMvc
            .perform(get(ENTITY_API_URL_ID, plate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plate.getId()))
            .andExpect(jsonPath("$.idrestaurant").value(DEFAULT_IDRESTAURANT))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    void getNonExistingPlate() throws Exception {
        // Get the plate
        restPlateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPlate() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        int databaseSizeBeforeUpdate = plateRepository.findAll().size();

        // Update the plate
        Plate updatedPlate = plateRepository.findById(plate.getId()).get();
        updatedPlate
            .idrestaurant(UPDATED_IDRESTAURANT)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .price(UPDATED_PRICE);
        PlateDTO plateDTO = plateMapper.toDto(updatedPlate);

        restPlateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plateDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
        Plate testPlate = plateList.get(plateList.size() - 1);
        assertThat(testPlate.getIdrestaurant()).isEqualTo(UPDATED_IDRESTAURANT);
        assertThat(testPlate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlate.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPlate.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testPlate.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    void putNonExistingPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlateWithPatch() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        int databaseSizeBeforeUpdate = plateRepository.findAll().size();

        // Update the plate using partial update
        Plate partialUpdatedPlate = new Plate();
        partialUpdatedPlate.setId(plate.getId());

        partialUpdatedPlate.price(UPDATED_PRICE);

        restPlateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlate))
            )
            .andExpect(status().isOk());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
        Plate testPlate = plateList.get(plateList.size() - 1);
        assertThat(testPlate.getIdrestaurant()).isEqualTo(DEFAULT_IDRESTAURANT);
        assertThat(testPlate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlate.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPlate.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testPlate.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    void fullUpdatePlateWithPatch() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        int databaseSizeBeforeUpdate = plateRepository.findAll().size();

        // Update the plate using partial update
        Plate partialUpdatedPlate = new Plate();
        partialUpdatedPlate.setId(plate.getId());

        partialUpdatedPlate
            .idrestaurant(UPDATED_IDRESTAURANT)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .price(UPDATED_PRICE);

        restPlateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlate))
            )
            .andExpect(status().isOk());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
        Plate testPlate = plateList.get(plateList.size() - 1);
        assertThat(testPlate.getIdrestaurant()).isEqualTo(UPDATED_IDRESTAURANT);
        assertThat(testPlate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlate.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPlate.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testPlate.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    void patchNonExistingPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlate() throws Exception {
        int databaseSizeBeforeUpdate = plateRepository.findAll().size();
        plate.setId(UUID.randomUUID().toString());

        // Create the Plate
        PlateDTO plateDTO = plateMapper.toDto(plate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plate in the database
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlate() throws Exception {
        // Initialize the database
        plateRepository.save(plate);

        int databaseSizeBeforeDelete = plateRepository.findAll().size();

        // Delete the plate
        restPlateMockMvc
            .perform(delete(ENTITY_API_URL_ID, plate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plate> plateList = plateRepository.findAll();
        assertThat(plateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
