import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import UserProfileService from './user-profile.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ShopUserService from '@/entities/shop-user/shop-user.service';
import { type IShopUser } from '@/shared/model/shop-user.model';
import { type IUserProfile, UserProfile } from '@/shared/model/user-profile.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserProfileUpdate',
  setup() {
    const userProfileService = inject('userProfileService', () => new UserProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const userProfile: Ref<IUserProfile> = ref(new UserProfile());

    const shopUserService = inject('shopUserService', () => new ShopUserService());

    const shopUsers: Ref<IShopUser[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveUserProfile = async userProfileId => {
      try {
        const res = await userProfileService().find(userProfileId);
        userProfile.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.userProfileId) {
      retrieveUserProfile(route.params.userProfileId);
    }

    const initRelationships = () => {
      shopUserService()
        .retrieve()
        .then(res => {
          shopUsers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      phoneNumber: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 15 }).toString(), 15),
      },
      address: {
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 255 }).toString(), 255),
      },
      shopUser: {},
    };
    const v$ = useVuelidate(validationRules, userProfile as any);
    v$.value.$validate();

    return {
      userProfileService,
      alertService,
      userProfile,
      previousState,
      isSaving,
      currentLanguage,
      shopUsers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.userProfile.id) {
        this.userProfileService()
          .update(this.userProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('mallApp.userProfile.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.userProfileService()
          .create(this.userProfile)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('mallApp.userProfile.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
