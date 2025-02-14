package com.custom.mall.service.mapper;

import com.custom.mall.domain.Order;
import com.custom.mall.domain.Product;
import com.custom.mall.domain.ShopUser;
import com.custom.mall.service.dto.OrderDTO;
import com.custom.mall.service.dto.ProductDTO;
import com.custom.mall.service.dto.ShopUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "shopUser", source = "shopUser", qualifiedByName = "shopUserId")
    OrderDTO toDto(Order s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("shopUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShopUserDTO toDtoShopUserId(ShopUser shopUser);
}
