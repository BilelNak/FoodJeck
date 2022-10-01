package com.foodjeck.platmanager.service;

import com.foodjeck.platmanager.client.RestaurantFeignClient;
import com.foodjeck.platmanager.domain.Plate;
import com.foodjeck.platmanager.repository.PlateRepository;
import com.foodjeck.platmanager.service.dto.PlateDTO;
import com.foodjeck.platmanager.service.dto.RestaurantDTO;
import com.foodjeck.platmanager.service.mapper.PlateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Plate}.
 */
@Service
public class PlateService {

    private final Logger log = LoggerFactory.getLogger(PlateService.class);

    private final PlateRepository plateRepository;

    private final PlateMapper plateMapper;
    private final RestaurantFeignClient restaurantFeignClient;

    public PlateService(PlateRepository plateRepository, PlateMapper plateMapper, RestaurantFeignClient restaurantFeignClient) {
        this.plateRepository = plateRepository;
        this.plateMapper = plateMapper;
        this.restaurantFeignClient = restaurantFeignClient;
    }

    /**
     * Save a plate.
     *
     * @param plateDTO the entity to save.
     * @return the persisted entity.
     */
    public PlateDTO save(PlateDTO plateDTO) {
        log.debug("Request to save Plate : {}", plateDTO);
        Plate plate = plateMapper.toEntity(plateDTO);
        plate = plateRepository.save(plate);
        return plateMapper.toDto(plate);
    }

    /**
     * Update a plate.
     *
     * @param plateDTO the entity to save.
     * @return the persisted entity.
     */
    public PlateDTO update(PlateDTO plateDTO) {
        log.debug("Request to update Plate : {}", plateDTO);
        Plate plate = plateMapper.toEntity(plateDTO);
        plate = plateRepository.save(plate);
        return plateMapper.toDto(plate);
    }

    /**
     * Partially update a plate.
     *
     * @param plateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlateDTO> partialUpdate(PlateDTO plateDTO) {
        log.debug("Request to partially update Plate : {}", plateDTO);

        return plateRepository
            .findById(plateDTO.getId())
            .map(existingPlate -> {
                plateMapper.partialUpdate(existingPlate, plateDTO);

                return existingPlate;
            })
            .map(plateRepository::save)
            .map(plateMapper::toDto);
    }

    /**
     * Get all the plates.
     *
     * @return the list of entities.
     */
    public List<PlateDTO> findAll() {
        log.debug("Request to get all Plates");
        return plateRepository.findAll().stream().map(plateMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public List<PlateDTO> findAllByIdRestaurant(String idRestaurant) {
        log.debug("Request to get all Plates by IdRestaurant");
        return plateRepository
            .findByIdrestaurant(idRestaurant)
            .stream()
            .map(plateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one plate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<PlateDTO> findOne(String id) {
        log.debug("Request to get Plate : {}", id);
        Optional<PlateDTO> pDTO = plateRepository.findById(id).map(plateMapper::toDto);

        ResponseEntity<RestaurantDTO> resDTO = restaurantFeignClient.getRestaurant(pDTO.get().getIdrestaurant());
        pDTO.get().setRestaurantDTO(resDTO.getBody());

        return pDTO;
    }

    /**
     * Delete the plate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Plate : {}", id);
        plateRepository.deleteById(id);
    }
}
