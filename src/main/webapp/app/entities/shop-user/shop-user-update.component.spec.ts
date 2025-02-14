import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ShopUserUpdate from './shop-user-update.vue';
import ShopUserService from './shop-user.service';
import AlertService from '@/shared/alert/alert.service';

type ShopUserUpdateComponentType = InstanceType<typeof ShopUserUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const shopUserSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ShopUserUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ShopUser Management Update Component', () => {
    let comp: ShopUserUpdateComponentType;
    let shopUserServiceStub: SinonStubbedInstance<ShopUserService>;

    beforeEach(() => {
      route = {};
      shopUserServiceStub = sinon.createStubInstance<ShopUserService>(ShopUserService);
      shopUserServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          shopUserService: () => shopUserServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ShopUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.shopUser = shopUserSample;
        shopUserServiceStub.update.resolves(shopUserSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(shopUserServiceStub.update.calledWith(shopUserSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        shopUserServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ShopUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.shopUser = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(shopUserServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        shopUserServiceStub.find.resolves(shopUserSample);
        shopUserServiceStub.retrieve.resolves([shopUserSample]);

        // WHEN
        route = {
          params: {
            shopUserId: `${shopUserSample.id}`,
          },
        };
        const wrapper = shallowMount(ShopUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.shopUser).toMatchObject(shopUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        shopUserServiceStub.find.resolves(shopUserSample);
        const wrapper = shallowMount(ShopUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
