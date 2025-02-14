package com.custom.mall.repository;

import com.custom.mall.domain.ShopUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShopUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopUserRepository extends JpaRepository<ShopUser, Long> {}
