import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import FlashSaleDetails from './flash-sale-details.vue';
import FlashSaleService from './flash-sale.service';
import AlertService from '@/shared/alert/alert.service';

type FlashSaleDetailsComponentType = InstanceType<typeof FlashSaleDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flashSaleSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('FlashSale Management Detail Component', () => {
    let flashSaleServiceStub: SinonStubbedInstance<FlashSaleService>;
    let mountOptions: MountingOptions<FlashSaleDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      flashSaleServiceStub = sinon.createStubInstance<FlashSaleService>(FlashSaleService);

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
          flashSaleService: () => flashSaleServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        flashSaleServiceStub.find.resolves(flashSaleSample);
        route = {
          params: {
            flashSaleId: `${123}`,
          },
        };
        const wrapper = shallowMount(FlashSaleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.flashSale).toMatchObject(flashSaleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flashSaleServiceStub.find.resolves(flashSaleSample);
        const wrapper = shallowMount(FlashSaleDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
