import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ShopUserService from './shop-user.service';
import { type IShopUser } from '@/shared/model/shop-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ShopUserDetails',
  setup() {
    const shopUserService = inject('shopUserService', () => new ShopUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const shopUser: Ref<IShopUser> = ref({});

    const retrieveShopUser = async shopUserId => {
      try {
        const res = await shopUserService().find(shopUserId);
        shopUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.shopUserId) {
      retrieveShopUser(route.params.shopUserId);
    }

    return {
      alertService,
      shopUser,

      previousState,
      t$: useI18n().t,
    };
  },
});
