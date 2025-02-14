import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import dayjs from 'dayjs';
import FlashSaleUpdate from './flash-sale-update.vue';
import FlashSaleService from './flash-sale.service';
import { DATE_TIME_LONG_FORMAT } from '@/shared/composables/date-format';
import AlertService from '@/shared/alert/alert.service';

type FlashSaleUpdateComponentType = InstanceType<typeof FlashSaleUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const flashSaleSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<FlashSaleUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('FlashSale Management Update Component', () => {
    let comp: FlashSaleUpdateComponentType;
    let flashSaleServiceStub: SinonStubbedInstance<FlashSaleService>;

    beforeEach(() => {
      route = {};
      flashSaleServiceStub = sinon.createStubInstance<FlashSaleService>(FlashSaleService);
      flashSaleServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          flashSaleService: () => flashSaleServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('load', () => {
      beforeEach(() => {
        const wrapper = shallowMount(FlashSaleUpdate, { global: mountOptions });
        comp = wrapper.vm;
      });
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(FlashSaleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flashSale = flashSaleSample;
        flashSaleServiceStub.update.resolves(flashSaleSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flashSaleServiceStub.update.calledWith(flashSaleSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        flashSaleServiceStub.create.resolves(entity);
        const wrapper = shallowMount(FlashSaleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.flashSale = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(flashSaleServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        flashSaleServiceStub.find.resolves(flashSaleSample);
        flashSaleServiceStub.retrieve.resolves([flashSaleSample]);

        // WHEN
        route = {
          params: {
            flashSaleId: `${flashSaleSample.id}`,
          },
        };
        const wrapper = shallowMount(FlashSaleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.flashSale).toMatchObject(flashSaleSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        flashSaleServiceStub.find.resolves(flashSaleSample);
        const wrapper = shallowMount(FlashSaleUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
