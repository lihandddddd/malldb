import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ProductService from './product.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import FlashSaleService from '@/entities/flash-sale/flash-sale.service';
import { type IFlashSale } from '@/shared/model/flash-sale.model';
import { type IProduct, Product } from '@/shared/model/product.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ProductUpdate',
  setup() {
    const productService = inject('productService', () => new ProductService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const product: Ref<IProduct> = ref(new Product());

    const flashSaleService = inject('flashSaleService', () => new FlashSaleService());

    const flashSales: Ref<IFlashSale[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveProduct = async productId => {
      try {
        const res = await productService().find(productId);
        product.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.productId) {
      retrieveProduct(route.params.productId);
    }

    const initRelationships = () => {
      flashSaleService()
        .retrieve()
        .then(res => {
          flashSales.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      description: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 1000 }).toString(), 1000),
      },
      price: {
        required: validations.required(t$('entity.validation.required').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
      },
      stock: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
        min: validations.minValue(t$('entity.validation.min', { min: 0 }).toString(), 0),
      },
      flashSale: {},
      orders: {},
    };
    const v$ = useVuelidate(validationRules, product as any);
    v$.value.$validate();

    return {
      productService,
      alertService,
      product,
      previousState,
      isSaving,
      currentLanguage,
      flashSales,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.product.id) {
        this.productService()
          .update(this.product)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('mallApp.product.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.productService()
          .create(this.product)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('mallApp.product.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
