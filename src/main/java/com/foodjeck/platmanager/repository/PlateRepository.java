package com.foodjeck.platmanager.repository;

import com.foodjeck.platmanager.domain.Plate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Plate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlateRepository extends MongoRepository<Plate, String> {
    List<Plate> findByIdrestaurant(String idrestaurant);
}
