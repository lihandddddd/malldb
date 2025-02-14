package com.custom.mall.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShopUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopUserDTO.class);
        ShopUserDTO shopUserDTO1 = new ShopUserDTO();
        shopUserDTO1.setId(1L);
        ShopUserDTO shopUserDTO2 = new ShopUserDTO();
        assertThat(shopUserDTO1).isNotEqualTo(shopUserDTO2);
        shopUserDTO2.setId(shopUserDTO1.getId());
        assertThat(shopUserDTO1).isEqualTo(shopUserDTO2);
        shopUserDTO2.setId(2L);
        assertThat(shopUserDTO1).isNotEqualTo(shopUserDTO2);
        shopUserDTO1.setId(null);
        assertThat(shopUserDTO1).isNotEqualTo(shopUserDTO2);
    }
}
