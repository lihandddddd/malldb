package com.custom.mall.domain;

import static com.custom.mall.domain.ShopUserTestSamples.*;
import static com.custom.mall.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.custom.mall.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void shopUserTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        ShopUser shopUserBack = getShopUserRandomSampleGenerator();

        userProfile.setShopUser(shopUserBack);
        assertThat(userProfile.getShopUser()).isEqualTo(shopUserBack);

        userProfile.shopUser(null);
        assertThat(userProfile.getShopUser()).isNull();
    }
}
