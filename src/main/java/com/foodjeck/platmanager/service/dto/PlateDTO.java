package com.foodjeck.platmanager.service.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * A DTO for the {@link com.foodjeck.platmanager.domain.Plate} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlateDTO implements Serializable {

    private String id;

    private String idrestaurant;

    private String name;

    private byte[] photo;

    private String photoContentType;
    private Double price;
    private RestaurantDTO restaurantDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdrestaurant() {
        return idrestaurant;
    }

    public void setIdrestaurant(String idrestaurant) {
        this.idrestaurant = idrestaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public RestaurantDTO getRestaurantDTO() {
        return restaurantDTO;
    }

    public void setRestaurantDTO(RestaurantDTO restaurantDTO) {
        this.restaurantDTO = restaurantDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlateDTO)) {
            return false;
        }

        PlateDTO plateDTO = (PlateDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, plateDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "PlateDTO [id=" +
            id +
            ", idrestaurant=" +
            idrestaurant +
            ", name=" +
            name +
            ", photo=" +
            Arrays.toString(photo) +
            ", photoContentType=" +
            photoContentType +
            ", price=" +
            price +
            ", restaurantDTO=" +
            restaurantDTO +
            "]"
        );
    }
    // prettier-ignore

}
