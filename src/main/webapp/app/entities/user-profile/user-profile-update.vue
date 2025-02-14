<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="mallApp.userProfile.home.createOrEditLabel"
          data-cy="UserProfileCreateUpdateHeading"
          v-text="t$('mallApp.userProfile.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="userProfile.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="userProfile.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.userProfile.phoneNumber')" for="user-profile-phoneNumber"></label>
            <input
              type="text"
              class="form-control"
              name="phoneNumber"
              id="user-profile-phoneNumber"
              data-cy="phoneNumber"
              :class="{ valid: !v$.phoneNumber.$invalid, invalid: v$.phoneNumber.$invalid }"
              v-model="v$.phoneNumber.$model"
            />
            <div v-if="v$.phoneNumber.$anyDirty && v$.phoneNumber.$invalid">
              <small class="form-text text-danger" v-for="error of v$.phoneNumber.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.userProfile.address')" for="user-profile-address"></label>
            <input
              type="text"
              class="form-control"
              name="address"
              id="user-profile-address"
              data-cy="address"
              :class="{ valid: !v$.address.$invalid, invalid: v$.address.$invalid }"
              v-model="v$.address.$model"
            />
            <div v-if="v$.address.$anyDirty && v$.address.$invalid">
              <small class="form-text text-danger" v-for="error of v$.address.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.userProfile.shopUser')" for="user-profile-shopUser"></label>
            <select class="form-control" id="user-profile-shopUser" data-cy="shopUser" name="shopUser" v-model="userProfile.shopUser">
              <option :value="null"></option>
              <option
                :value="userProfile.shopUser && shopUserOption.id === userProfile.shopUser.id ? userProfile.shopUser : shopUserOption"
                v-for="shopUserOption in shopUsers"
                :key="shopUserOption.id"
              >
                {{ shopUserOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./user-profile-update.component.ts"></script>
