import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UserProfileUpdate from './user-profile-update.vue';
import UserProfileService from './user-profile.service';
import AlertService from '@/shared/alert/alert.service';

import ShopUserService from '@/entities/shop-user/shop-user.service';

type UserProfileUpdateComponentType = InstanceType<typeof UserProfileUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userProfileSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<UserProfileUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('UserProfile Management Update Component', () => {
    let comp: UserProfileUpdateComponentType;
    let userProfileServiceStub: SinonStubbedInstance<UserProfileService>;

    beforeEach(() => {
      route = {};
      userProfileServiceStub = sinon.createStubInstance<UserProfileService>(UserProfileService);
      userProfileServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          userProfileService: () => userProfileServiceStub,
          shopUserService: () =>
            sinon.createStubInstance<ShopUserService>(ShopUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(UserProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userProfile = userProfileSample;
        userProfileServiceStub.update.resolves(userProfileSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userProfileServiceStub.update.calledWith(userProfileSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        userProfileServiceStub.create.resolves(entity);
        const wrapper = shallowMount(UserProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.userProfile = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(userProfileServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        userProfileServiceStub.find.resolves(userProfileSample);
        userProfileServiceStub.retrieve.resolves([userProfileSample]);

        // WHEN
        route = {
          params: {
            userProfileId: `${userProfileSample.id}`,
          },
        };
        const wrapper = shallowMount(UserProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.userProfile).toMatchObject(userProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userProfileServiceStub.find.resolves(userProfileSample);
        const wrapper = shallowMount(UserProfileUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
