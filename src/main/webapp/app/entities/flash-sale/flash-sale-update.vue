<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="mallApp.flashSale.home.createOrEditLabel"
          data-cy="FlashSaleCreateUpdateHeading"
          v-text="t$('mallApp.flashSale.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="flashSale.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="flashSale.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.flashSale.name')" for="flash-sale-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="flash-sale-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
              required
            />
            <div v-if="v$.name.$anyDirty && v$.name.$invalid">
              <small class="form-text text-danger" v-for="error of v$.name.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.flashSale.startTime')" for="flash-sale-startTime"></label>
            <div class="d-flex">
              <input
                id="flash-sale-startTime"
                data-cy="startTime"
                type="datetime-local"
                class="form-control"
                name="startTime"
                :class="{ valid: !v$.startTime.$invalid, invalid: v$.startTime.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.startTime.$model)"
                @change="updateZonedDateTimeField('startTime', $event)"
              />
            </div>
            <div v-if="v$.startTime.$anyDirty && v$.startTime.$invalid">
              <small class="form-text text-danger" v-for="error of v$.startTime.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.flashSale.endTime')" for="flash-sale-endTime"></label>
            <div class="d-flex">
              <input
                id="flash-sale-endTime"
                data-cy="endTime"
                type="datetime-local"
                class="form-control"
                name="endTime"
                :class="{ valid: !v$.endTime.$invalid, invalid: v$.endTime.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.endTime.$model)"
                @change="updateZonedDateTimeField('endTime', $event)"
              />
            </div>
            <div v-if="v$.endTime.$anyDirty && v$.endTime.$invalid">
              <small class="form-text text-danger" v-for="error of v$.endTime.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.flashSale.discountRate')" for="flash-sale-discountRate"></label>
            <input
              type="number"
              class="form-control"
              name="discountRate"
              id="flash-sale-discountRate"
              data-cy="discountRate"
              :class="{ valid: !v$.discountRate.$invalid, invalid: v$.discountRate.$invalid }"
              v-model.number="v$.discountRate.$model"
              required
            />
            <div v-if="v$.discountRate.$anyDirty && v$.discountRate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.discountRate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.flashSale.maxQuantity')" for="flash-sale-maxQuantity"></label>
            <input
              type="number"
              class="form-control"
              name="maxQuantity"
              id="flash-sale-maxQuantity"
              data-cy="maxQuantity"
              :class="{ valid: !v$.maxQuantity.$invalid, invalid: v$.maxQuantity.$invalid }"
              v-model.number="v$.maxQuantity.$model"
              required
            />
            <div v-if="v$.maxQuantity.$anyDirty && v$.maxQuantity.$invalid">
              <small class="form-text text-danger" v-for="error of v$.maxQuantity.$errors" :key="error.$uid">{{ error.$message }}</small>
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
<script lang="ts" src="./flash-sale-update.component.ts"></script>
