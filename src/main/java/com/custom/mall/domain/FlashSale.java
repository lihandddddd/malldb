package com.custom.mall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FlashSale.
 */
@Entity
@Table(name = "flash_sale")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "flashsale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FlashSale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "name", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private ZonedDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "1")
    @Column(name = "discount_rate", nullable = false)
    private Float discountRate;

    @NotNull
    @Min(value = 1)
    @Column(name = "max_quantity", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer maxQuantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "flashSale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "flashSale", "orders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FlashSale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FlashSale name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    public FlashSale startTime(ZonedDateTime startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    public FlashSale endTime(ZonedDateTime endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Float getDiscountRate() {
        return this.discountRate;
    }

    public FlashSale discountRate(Float discountRate) {
        this.setDiscountRate(discountRate);
        return this;
    }

    public void setDiscountRate(Float discountRate) {
        this.discountRate = discountRate;
    }

    public Integer getMaxQuantity() {
        return this.maxQuantity;
    }

    public FlashSale maxQuantity(Integer maxQuantity) {
        this.setMaxQuantity(maxQuantity);
        return this;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setFlashSale(null));
        }
        if (products != null) {
            products.forEach(i -> i.setFlashSale(this));
        }
        this.products = products;
    }

    public FlashSale products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public FlashSale addProduct(Product product) {
        this.products.add(product);
        product.setFlashSale(this);
        return this;
    }

    public FlashSale removeProduct(Product product) {
        this.products.remove(product);
        product.setFlashSale(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlashSale)) {
            return false;
        }
        return getId() != null && getId().equals(((FlashSale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FlashSale{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", maxQuantity=" + getMaxQuantity() +
            "}";
    }
}
