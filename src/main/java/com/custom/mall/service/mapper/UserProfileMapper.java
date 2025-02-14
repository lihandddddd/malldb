package com.custom.mall.service.mapper;

import com.custom.mall.domain.ShopUser;
import com.custom.mall.domain.UserProfile;
import com.custom.mall.service.dto.ShopUserDTO;
import com.custom.mall.service.dto.UserProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {
    @Mapping(target = "shopUser", source = "shopUser", qualifiedByName = "shopUserId")
    UserProfileDTO toDto(UserProfile s);

    @Named("shopUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShopUserDTO toDtoShopUserId(ShopUser shopUser);
}
