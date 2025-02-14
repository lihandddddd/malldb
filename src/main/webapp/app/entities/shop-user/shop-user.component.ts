import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ShopUserService from './shop-user.service';
import { type IShopUser } from '@/shared/model/shop-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ShopUser',
  setup() {
    const { t: t$ } = useI18n();
    const shopUserService = inject('shopUserService', () => new ShopUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const currentSearch = ref('');

    const shopUsers: Ref<IShopUser[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {
      currentSearch.value = '';
    };

    const retrieveShopUsers = async () => {
      isFetching.value = true;
      try {
        const res = currentSearch.value ? await shopUserService().search(currentSearch.value) : await shopUserService().retrieve();
        shopUsers.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveShopUsers();
    };

    onMounted(async () => {
      await retrieveShopUsers();
    });

    const search = query => {
      if (!query) {
        return clear();
      }
      currentSearch.value = query;
      retrieveShopUsers();
    };

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IShopUser) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeShopUser = async () => {
      try {
        await shopUserService().delete(removeId.value);
        const message = t$('mallApp.shopUser.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveShopUsers();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      shopUsers,
      handleSyncList,
      isFetching,
      retrieveShopUsers,
      clear,
      currentSearch,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeShopUser,
      t$,
    };
  },
});
