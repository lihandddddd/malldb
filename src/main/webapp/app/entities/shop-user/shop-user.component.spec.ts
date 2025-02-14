import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ShopUser from './shop-user.vue';
import ShopUserService from './shop-user.service';
import AlertService from '@/shared/alert/alert.service';

type ShopUserComponentType = InstanceType<typeof ShopUser>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ShopUser Management Component', () => {
    let shopUserServiceStub: SinonStubbedInstance<ShopUserService>;
    let mountOptions: MountingOptions<ShopUserComponentType>['global'];

    beforeEach(() => {
      shopUserServiceStub = sinon.createStubInstance<ShopUserService>(ShopUserService);
      shopUserServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          shopUserService: () => shopUserServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        shopUserServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ShopUser, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(shopUserServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.shopUsers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ShopUserComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ShopUser, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        shopUserServiceStub.retrieve.reset();
        shopUserServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        shopUserServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeShopUser();
        await comp.$nextTick(); // clear components

        // THEN
        expect(shopUserServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(shopUserServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
