<template>
  <div>
    <h2 id="page-heading" data-cy="OrderHeading">
      <span v-text="t$('mallApp.order.home.title')" id="order-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('mallApp.order.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'OrderCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-order"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('mallApp.order.home.createLabel')"></span>
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
              :placeholder="t$('mallApp.order.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && orders && orders.length === 0">
      <span v-text="t$('mallApp.order.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="orders && orders.length > 0">
      <table class="table table-striped" aria-describedby="orders">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('orderDate')">
              <span v-text="t$('mallApp.order.orderDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'orderDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('totalAmount')">
              <span v-text="t$('mallApp.order.totalAmount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'totalAmount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('mallApp.order.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('product.id')">
              <span v-text="t$('mallApp.order.product')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'product.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('shopUser.id')">
              <span v-text="t$('mallApp.order.shopUser')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'shopUser.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OrderView', params: { orderId: order.id } }">{{ order.id }}</router-link>
            </td>
            <td>{{ formatDateShort(order.orderDate) || '' }}</td>
            <td>{{ order.totalAmount }}</td>
            <td v-text="t$('mallApp.OrderStatus.' + order.status)"></td>
            <td>
              <div v-if="order.product">
                <router-link :to="{ name: 'ProductView', params: { productId: order.product.id } }">{{ order.product.id }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="order.shopUser">
                <router-link :to="{ name: 'ShopUserView', params: { shopUserId: order.shopUser.id } }">{{ order.shopUser.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'OrderView', params: { orderId: order.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'OrderEdit', params: { orderId: order.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(order)"
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
        <span id="mallApp.order.delete.question" data-cy="orderDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-order-heading" v-text="t$('mallApp.order.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-order"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeOrder()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="orders && orders.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./order.component.ts"></script>
