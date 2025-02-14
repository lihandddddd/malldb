import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import UserProfile from './user-profile.vue';
import UserProfileService from './user-profile.service';
import AlertService from '@/shared/alert/alert.service';

type UserProfileComponentType = InstanceType<typeof UserProfile>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('UserProfile Management Component', () => {
    let userProfileServiceStub: SinonStubbedInstance<UserProfileService>;
    let mountOptions: MountingOptions<UserProfileComponentType>['global'];

    beforeEach(() => {
      userProfileServiceStub = sinon.createStubInstance<UserProfileService>(UserProfileService);
      userProfileServiceStub.retrieve.resolves({ headers: {} });

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
          userProfileService: () => userProfileServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        userProfileServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(UserProfile, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(userProfileServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.userProfiles[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: UserProfileComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(UserProfile, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        userProfileServiceStub.retrieve.reset();
        userProfileServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        userProfileServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeUserProfile();
        await comp.$nextTick(); // clear components

        // THEN
        expect(userProfileServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(userProfileServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
