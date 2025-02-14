<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="userProfile">
        <h2 class="jh-entity-heading" data-cy="userProfileDetailsHeading">
          <span v-text="t$('mallApp.userProfile.detail.title')"></span> {{ userProfile.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('mallApp.userProfile.phoneNumber')"></span>
          </dt>
          <dd>
            <span>{{ userProfile.phoneNumber }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.userProfile.address')"></span>
          </dt>
          <dd>
            <span>{{ userProfile.address }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.userProfile.shopUser')"></span>
          </dt>
          <dd>
            <div v-if="userProfile.shopUser">
              <router-link :to="{ name: 'ShopUserView', params: { shopUserId: userProfile.shopUser.id } }">{{
                userProfile.shopUser.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="userProfile.id"
          :to="{ name: 'UserProfileEdit', params: { userProfileId: userProfile.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./user-profile-details.component.ts"></script>
