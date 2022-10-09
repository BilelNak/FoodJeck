package com.foodjeck.delivery.service.mapper;

import com.foodjeck.delivery.domain.Restaurant;
import com.foodjeck.delivery.service.dto.RestaurantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Restaurant} and its DTO {@link RestaurantDTO}.
 */
@Mapper(componentModel = "spring")
public interface RestaurantMapper extends EntityMapper<RestaurantDTO, Restaurant> {}
