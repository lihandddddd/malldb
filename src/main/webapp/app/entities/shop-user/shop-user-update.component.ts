import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ShopUserService from './shop-user.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IShopUser, ShopUser } from '@/shared/model/shop-user.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ShopUserUpdate',
  setup() {
    const shopUserService = inject('shopUserService', () => new ShopUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const shopUser: Ref<IShopUser> = ref(new ShopUser());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {};

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      username: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 3 }).toString(), 3),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 100 }).toString(), 100),
      },
      email: {
        required: validations.required(t$('entity.validation.required').toString()),
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 5 }).toString(), 5),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      orders: {},
      userProfile: {},
    };
    const v$ = useVuelidate(validationRules, shopUser as any);
    v$.value.$validate();

    return {
      shopUserService,
      alertService,
      shopUser,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.shopUser.id) {
        this.shopUserService()
          .update(this.shopUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('mallApp.shopUser.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.shopUserService()
          .create(this.shopUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('mallApp.shopUser.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
