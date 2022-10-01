package com.foodjeck.platmanager.client;

import com.foodjeck.platmanager.service.dto.RestaurantDTO;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "127.0.0.1:8081")
@Headers({ "Accept: application/json", "Content-Type: application/json" })
public interface RestaurantFeignClient {
    @GetMapping("api/restaurants/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable("id") String id);
}
