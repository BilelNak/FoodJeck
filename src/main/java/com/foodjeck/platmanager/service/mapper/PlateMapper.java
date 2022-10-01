package com.foodjeck.platmanager.service.mapper;

import com.foodjeck.platmanager.domain.Plate;
import com.foodjeck.platmanager.service.dto.PlateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plate} and its DTO {@link PlateDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlateMapper extends EntityMapper<PlateDTO, Plate> {}
