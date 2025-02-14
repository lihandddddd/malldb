import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ShopUserDetails from './shop-user-details.vue';
import ShopUserService from './shop-user.service';
import AlertService from '@/shared/alert/alert.service';

type ShopUserDetailsComponentType = InstanceType<typeof ShopUserDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const shopUserSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ShopUser Management Detail Component', () => {
    let shopUserServiceStub: SinonStubbedInstance<ShopUserService>;
    let mountOptions: MountingOptions<ShopUserDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      shopUserServiceStub = sinon.createStubInstance<ShopUserService>(ShopUserService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          shopUserService: () => shopUserServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        shopUserServiceStub.find.resolves(shopUserSample);
        route = {
          params: {
            shopUserId: `${123}`,
          },
        };
        const wrapper = shallowMount(ShopUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.shopUser).toMatchObject(shopUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        shopUserServiceStub.find.resolves(shopUserSample);
        const wrapper = shallowMount(ShopUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
