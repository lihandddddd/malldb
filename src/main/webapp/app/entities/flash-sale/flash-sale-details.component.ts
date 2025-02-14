import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import FlashSaleService from './flash-sale.service';
import { useDateFormat } from '@/shared/composables';
import { type IFlashSale } from '@/shared/model/flash-sale.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlashSaleDetails',
  setup() {
    const dateFormat = useDateFormat();
    const flashSaleService = inject('flashSaleService', () => new FlashSaleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const flashSale: Ref<IFlashSale> = ref({});

    const retrieveFlashSale = async flashSaleId => {
      try {
        const res = await flashSaleService().find(flashSaleId);
        flashSale.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.flashSaleId) {
      retrieveFlashSale(route.params.flashSaleId);
    }

    return {
      ...dateFormat,
      alertService,
      flashSale,

      previousState,
      t$: useI18n().t,
    };
  },
});
