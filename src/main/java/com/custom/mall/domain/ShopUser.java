package com.custom.mall.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShopUser.
 */
@Entity
@Table(name = "shop_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shopuser")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShopUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "username", length = 100, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String username;

    @NotNull
    @Size(min = 5, max = 255)
    @Column(name = "email", length = 255, nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shopUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "product", "shopUser" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @JsonIgnoreProperties(value = { "shopUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "shopUser")
    @org.springframework.data.annotation.Transient
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShopUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public ShopUser username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public ShopUser email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setShopUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setShopUser(this));
        }
        this.orders = orders;
    }

    public ShopUser orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public ShopUser addOrder(Order order) {
        this.orders.add(order);
        order.setShopUser(this);
        return this;
    }

    public ShopUser removeOrder(Order order) {
        this.orders.remove(order);
        order.setShopUser(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            this.userProfile.setShopUser(null);
        }
        if (userProfile != null) {
            userProfile.setShopUser(this);
        }
        this.userProfile = userProfile;
    }

    public ShopUser userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShopUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ShopUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShopUser{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
