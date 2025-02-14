package com.custom.mall.service.mapper;

import com.custom.mall.domain.FlashSale;
import com.custom.mall.service.dto.FlashSaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FlashSale} and its DTO {@link FlashSaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface FlashSaleMapper extends EntityMapper<FlashSaleDTO, FlashSale> {}
