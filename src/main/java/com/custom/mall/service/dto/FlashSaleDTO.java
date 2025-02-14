package com.custom.mall.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.custom.mall.domain.FlashSale} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FlashSaleDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    @NotNull
    private ZonedDateTime startTime;

    @NotNull
    private ZonedDateTime endTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    private Float discountRate;

    @NotNull
    @Min(value = 1)
    private Integer maxQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Float discountRate) {
        this.discountRate = discountRate;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlashSaleDTO)) {
            return false;
        }

        FlashSaleDTO flashSaleDTO = (FlashSaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, flashSaleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlashSaleDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", maxQuantity=" + getMaxQuantity() +
            "}";
    }
}
