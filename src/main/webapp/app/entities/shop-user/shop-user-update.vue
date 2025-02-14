<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="mallApp.shopUser.home.createOrEditLabel"
          data-cy="ShopUserCreateUpdateHeading"
          v-text="t$('mallApp.shopUser.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="shopUser.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="shopUser.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.shopUser.username')" for="shop-user-username"></label>
            <input
              type="text"
              class="form-control"
              name="username"
              id="shop-user-username"
              data-cy="username"
              :class="{ valid: !v$.username.$invalid, invalid: v$.username.$invalid }"
              v-model="v$.username.$model"
              required
            />
            <div v-if="v$.username.$anyDirty && v$.username.$invalid">
              <small class="form-text text-danger" v-for="error of v$.username.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.shopUser.email')" for="shop-user-email"></label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="shop-user-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
              required
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
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
<script lang="ts" src="./shop-user-update.component.ts"></script>
