import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import FlashSaleService from './flash-sale.service';
import { useDateFormat, useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { FlashSale, type IFlashSale } from '@/shared/model/flash-sale.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'FlashSaleUpdate',
  setup() {
    const flashSaleService = inject('flashSaleService', () => new FlashSaleService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const flashSale: Ref<IFlashSale> = ref(new FlashSale());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveFlashSale = async flashSaleId => {
      try {
        const res = await flashSaleService().find(flashSaleId);
        res.startTime = new Date(res.startTime);
        res.endTime = new Date(res.endTime);
        flashSale.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.flashSaleId) {
      retrieveFlashSale(route.params.flashSaleId);
    }

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      startTime: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      endTime: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      discountRate: {
        required: validations.required(t$('entity.validation.required').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
        max: validations.maxValue(t$('entity.validation.max', { max: 1 }).toString(), 1),
      },
      maxQuantity: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 1 }).toString(), 1),
      },
      products: {},
    };
    const v$ = useVuelidate(validationRules, flashSale as any);
    v$.value.$validate();

    return {
      flashSaleService,
      alertService,
      flashSale,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      ...useDateFormat({ entityRef: flashSale }),
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.flashSale.id) {
        this.flashSaleService()
          .update(this.flashSale)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('mallApp.flashSale.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.flashSaleService()
          .create(this.flashSale)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('mallApp.flashSale.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
