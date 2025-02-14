import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import UserProfileService from './user-profile.service';
import { type IUserProfile } from '@/shared/model/user-profile.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserProfileDetails',
  setup() {
    const userProfileService = inject('userProfileService', () => new UserProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const userProfile: Ref<IUserProfile> = ref({});

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

    return {
      alertService,
      userProfile,

      previousState,
      t$: useI18n().t,
    };
  },
});
