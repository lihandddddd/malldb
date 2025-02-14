package com.custom.mall.service.mapper;

import com.custom.mall.domain.FlashSale;
import com.custom.mall.domain.Product;
import com.custom.mall.service.dto.FlashSaleDTO;
import com.custom.mall.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "flashSale", source = "flashSale", qualifiedByName = "flashSaleId")
    ProductDTO toDto(Product s);

    @Named("flashSaleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FlashSaleDTO toDtoFlashSaleId(FlashSale flashSale);
}
