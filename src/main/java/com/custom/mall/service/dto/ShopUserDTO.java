package com.custom.mall.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.custom.mall.domain.ShopUser} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShopUserDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String username;

    @NotNull
    @Size(min = 5, max = 255)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopUserDTO)) {
            return false;
        }

        ShopUserDTO shopUserDTO = (ShopUserDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shopUserDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopUserDTO{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
