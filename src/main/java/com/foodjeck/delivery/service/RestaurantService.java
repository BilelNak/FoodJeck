package com.foodjeck.delivery.service;

import com.foodjeck.delivery.domain.Restaurant;
import com.foodjeck.delivery.repository.RestaurantRepository;
import com.foodjeck.delivery.service.dto.RestaurantDTO;
import com.foodjeck.delivery.service.mapper.RestaurantMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
public class RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
    }

    /**
     * Save a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantDTO save(RestaurantDTO restaurantDTO) {
        log.debug("Request to save Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    /**
     * Update a restaurant.
     *
     * @param restaurantDTO the entity to save.
     * @return the persisted entity.
     */
    public RestaurantDTO update(RestaurantDTO restaurantDTO) {
        log.debug("Request to update Restaurant : {}", restaurantDTO);
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(restaurant);
    }

    /**
     * Partially update a restaurant.
     *
     * @param restaurantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RestaurantDTO> partialUpdate(RestaurantDTO restaurantDTO) {
        log.debug("Request to partially update Restaurant : {}", restaurantDTO);

        return restaurantRepository
            .findById(restaurantDTO.getId())
            .map(existingRestaurant -> {
                restaurantMapper.partialUpdate(existingRestaurant, restaurantDTO);

                return existingRestaurant;
            })
            .map(restaurantRepository::save)
            .map(restaurantMapper::toDto);
    }

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    public List<RestaurantDTO> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAll().stream().map(restaurantMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one restaurant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<RestaurantDTO> findOne(String id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findById(id).map(restaurantMapper::toDto);
    }

    /**
     * Delete the restaurant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(String id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
    }
}
