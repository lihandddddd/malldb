<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="mallApp.product.home.createOrEditLabel"
          data-cy="ProductCreateUpdateHeading"
          v-text="t$('mallApp.product.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="product.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="product.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.product.name')" for="product-name"></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="product-name"
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
            <label class="form-control-label" v-text="t$('mallApp.product.description')" for="product-description"></label>
            <input
              type="text"
              class="form-control"
              name="description"
              id="product-description"
              data-cy="description"
              :class="{ valid: !v$.description.$invalid, invalid: v$.description.$invalid }"
              v-model="v$.description.$model"
            />
            <div v-if="v$.description.$anyDirty && v$.description.$invalid">
              <small class="form-text text-danger" v-for="error of v$.description.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.product.price')" for="product-price"></label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="product-price"
              data-cy="price"
              :class="{ valid: !v$.price.$invalid, invalid: v$.price.$invalid }"
              v-model.number="v$.price.$model"
              required
            />
            <div v-if="v$.price.$anyDirty && v$.price.$invalid">
              <small class="form-text text-danger" v-for="error of v$.price.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.product.stock')" for="product-stock"></label>
            <input
              type="number"
              class="form-control"
              name="stock"
              id="product-stock"
              data-cy="stock"
              :class="{ valid: !v$.stock.$invalid, invalid: v$.stock.$invalid }"
              v-model.number="v$.stock.$model"
              required
            />
            <div v-if="v$.stock.$anyDirty && v$.stock.$invalid">
              <small class="form-text text-danger" v-for="error of v$.stock.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.product.flashSale')" for="product-flashSale"></label>
            <select class="form-control" id="product-flashSale" data-cy="flashSale" name="flashSale" v-model="product.flashSale">
              <option :value="null"></option>
              <option
                :value="product.flashSale && flashSaleOption.id === product.flashSale.id ? product.flashSale : flashSaleOption"
                v-for="flashSaleOption in flashSales"
                :key="flashSaleOption.id"
              >
                {{ flashSaleOption.id }}
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
<script lang="ts" src="./product-update.component.ts"></script>
