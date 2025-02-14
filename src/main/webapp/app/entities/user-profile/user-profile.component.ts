import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import UserProfileService from './user-profile.service';
import { type IUserProfile } from '@/shared/model/user-profile.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'UserProfile',
  setup() {
    const { t: t$ } = useI18n();
    const userProfileService = inject('userProfileService', () => new UserProfileService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currentSearch = ref('');

    const userProfiles: Ref<IUserProfile[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {
      currentSearch.value = '';
    };

    const retrieveUserProfiles = async () => {
      isFetching.value = true;
      try {
        const res = currentSearch.value ? await userProfileService().search(currentSearch.value) : await userProfileService().retrieve();
        userProfiles.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveUserProfiles();
    };

    onMounted(async () => {
      await retrieveUserProfiles();
    });

    const search = query => {
      if (!query) {
        return clear();
      }
      currentSearch.value = query;
      retrieveUserProfiles();
    };

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IUserProfile) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeUserProfile = async () => {
      try {
        await userProfileService().delete(removeId.value);
        const message = t$('mallApp.userProfile.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveUserProfiles();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      userProfiles,
      handleSyncList,
      isFetching,
      retrieveUserProfiles,
      clear,
      currentSearch,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeUserProfile,
      t$,
    };
  },
});
