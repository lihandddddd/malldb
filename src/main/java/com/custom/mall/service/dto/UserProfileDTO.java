package com.custom.mall.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.custom.mall.domain.UserProfile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfileDTO implements Serializable {

    private Long id;

    @Size(max = 15)
    private String phoneNumber;

    @Size(max = 255)
    private String address;

    private ShopUserDTO shopUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ShopUserDTO getShopUser() {
        return shopUser;
    }

    public void setShopUser(ShopUserDTO shopUser) {
        this.shopUser = shopUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfileDTO)) {
            return false;
        }

        UserProfileDTO userProfileDTO = (UserProfileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userProfileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfileDTO{" +
            "id=" + getId() +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", shopUser=" + getShopUser() +
            "}";
    }
}
