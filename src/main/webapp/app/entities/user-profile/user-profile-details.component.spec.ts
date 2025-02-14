import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import UserProfileDetails from './user-profile-details.vue';
import UserProfileService from './user-profile.service';
import AlertService from '@/shared/alert/alert.service';

type UserProfileDetailsComponentType = InstanceType<typeof UserProfileDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const userProfileSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('UserProfile Management Detail Component', () => {
    let userProfileServiceStub: SinonStubbedInstance<UserProfileService>;
    let mountOptions: MountingOptions<UserProfileDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      userProfileServiceStub = sinon.createStubInstance<UserProfileService>(UserProfileService);

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
          userProfileService: () => userProfileServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userProfileServiceStub.find.resolves(userProfileSample);
        route = {
          params: {
            userProfileId: `${123}`,
          },
        };
        const wrapper = shallowMount(UserProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.userProfile).toMatchObject(userProfileSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        userProfileServiceStub.find.resolves(userProfileSample);
        const wrapper = shallowMount(UserProfileDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
