<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2
          id="mallApp.order.home.createOrEditLabel"
          data-cy="OrderCreateUpdateHeading"
          v-text="t$('mallApp.order.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="order.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="order.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.order.orderDate')" for="order-orderDate"></label>
            <div class="d-flex">
              <input
                id="order-orderDate"
                data-cy="orderDate"
                type="datetime-local"
                class="form-control"
                name="orderDate"
                :class="{ valid: !v$.orderDate.$invalid, invalid: v$.orderDate.$invalid }"
                required
                :value="convertDateTimeFromServer(v$.orderDate.$model)"
                @change="updateZonedDateTimeField('orderDate', $event)"
              />
            </div>
            <div v-if="v$.orderDate.$anyDirty && v$.orderDate.$invalid">
              <small class="form-text text-danger" v-for="error of v$.orderDate.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.order.totalAmount')" for="order-totalAmount"></label>
            <input
              type="number"
              class="form-control"
              name="totalAmount"
              id="order-totalAmount"
              data-cy="totalAmount"
              :class="{ valid: !v$.totalAmount.$invalid, invalid: v$.totalAmount.$invalid }"
              v-model.number="v$.totalAmount.$model"
              required
            />
            <div v-if="v$.totalAmount.$anyDirty && v$.totalAmount.$invalid">
              <small class="form-text text-danger" v-for="error of v$.totalAmount.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.order.status')" for="order-status"></label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !v$.status.$invalid, invalid: v$.status.$invalid }"
              v-model="v$.status.$model"
              id="order-status"
              data-cy="status"
              required
            >
              <option
                v-for="orderStatus in orderStatusValues"
                :key="orderStatus"
                :value="orderStatus"
                :label="t$('mallApp.OrderStatus.' + orderStatus)"
              >
                {{ orderStatus }}
              </option>
            </select>
            <div v-if="v$.status.$anyDirty && v$.status.$invalid">
              <small class="form-text text-danger" v-for="error of v$.status.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.order.product')" for="order-product"></label>
            <select class="form-control" id="order-product" data-cy="product" name="product" v-model="order.product">
              <option :value="null"></option>
              <option
                :value="order.product && productOption.id === order.product.id ? order.product : productOption"
                v-for="productOption in products"
                :key="productOption.id"
              >
                {{ productOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="t$('mallApp.order.shopUser')" for="order-shopUser"></label>
            <select class="form-control" id="order-shopUser" data-cy="shopUser" name="shopUser" v-model="order.shopUser">
              <option :value="null"></option>
              <option
                :value="order.shopUser && shopUserOption.id === order.shopUser.id ? order.shopUser : shopUserOption"
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
<script lang="ts" src="./order-update.component.ts"></script>
