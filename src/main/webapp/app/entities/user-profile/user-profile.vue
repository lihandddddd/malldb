<template>
  <div>
    <h2 id="page-heading" data-cy="UserProfileHeading">
      <span v-text="t$('mallApp.userProfile.home.title')" id="user-profile-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('mallApp.userProfile.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'UserProfileCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-user-profile"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('mallApp.userProfile.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <div class="row">
      <div class="col-sm-12">
        <form name="searchForm" class="form-inline" @submit.prevent="search(currentSearch)">
          <div class="input-group w-100 mt-3">
            <input
              type="text"
              class="form-control"
              name="currentSearch"
              id="currentSearch"
              :placeholder="t$('mallApp.userProfile.home.search')"
              v-model="currentSearch"
            />
            <button type="button" id="launch-search" class="btn btn-primary" @click="search(currentSearch)">
              <font-awesome-icon icon="search"></font-awesome-icon>
            </button>
            <button type="button" id="clear-search" class="btn btn-secondary" @click="clear()" v-if="currentSearch">
              <font-awesome-icon icon="trash"></font-awesome-icon>
            </button>
          </div>
        </form>
      </div>
    </div>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && userProfiles && userProfiles.length === 0">
      <span v-text="t$('mallApp.userProfile.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="userProfiles && userProfiles.length > 0">
      <table class="table table-striped" aria-describedby="userProfiles">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('mallApp.userProfile.phoneNumber')"></span></th>
            <th scope="row"><span v-text="t$('mallApp.userProfile.address')"></span></th>
            <th scope="row"><span v-text="t$('mallApp.userProfile.shopUser')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="userProfile in userProfiles" :key="userProfile.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'UserProfileView', params: { userProfileId: userProfile.id } }">{{ userProfile.id }}</router-link>
            </td>
            <td>{{ userProfile.phoneNumber }}</td>
            <td>{{ userProfile.address }}</td>
            <td>
              <div v-if="userProfile.shopUser">
                <router-link :to="{ name: 'ShopUserView', params: { shopUserId: userProfile.shopUser.id } }">{{
                  userProfile.shopUser.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'UserProfileView', params: { userProfileId: userProfile.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'UserProfileEdit', params: { userProfileId: userProfile.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(userProfile)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span id="mallApp.userProfile.delete.question" data-cy="userProfileDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-userProfile-heading" v-text="t$('mallApp.userProfile.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-userProfile"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeUserProfile()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./user-profile.component.ts"></script>
