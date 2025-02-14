package com.custom.mall.service.mapper;

import com.custom.mall.domain.ShopUser;
import com.custom.mall.service.dto.ShopUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShopUser} and its DTO {@link ShopUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShopUserMapper extends EntityMapper<ShopUserDTO, ShopUser> {}
