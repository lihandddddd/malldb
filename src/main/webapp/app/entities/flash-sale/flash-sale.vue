<template>
  <div>
    <h2 id="page-heading" data-cy="FlashSaleHeading">
      <span v-text="t$('mallApp.flashSale.home.title')" id="flash-sale-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('mallApp.flashSale.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'FlashSaleCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-flash-sale"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('mallApp.flashSale.home.createLabel')"></span>
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
              :placeholder="t$('mallApp.flashSale.home.search')"
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
    <div class="alert alert-warning" v-if="!isFetching && flashSales && flashSales.length === 0">
      <span v-text="t$('mallApp.flashSale.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="flashSales && flashSales.length > 0">
      <table class="table table-striped" aria-describedby="flashSales">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('name')">
              <span v-text="t$('mallApp.flashSale.name')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('startTime')">
              <span v-text="t$('mallApp.flashSale.startTime')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('endTime')">
              <span v-text="t$('mallApp.flashSale.endTime')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endTime'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('discountRate')">
              <span v-text="t$('mallApp.flashSale.discountRate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'discountRate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('maxQuantity')">
              <span v-text="t$('mallApp.flashSale.maxQuantity')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'maxQuantity'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="flashSale in flashSales" :key="flashSale.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'FlashSaleView', params: { flashSaleId: flashSale.id } }">{{ flashSale.id }}</router-link>
            </td>
            <td>{{ flashSale.name }}</td>
            <td>{{ formatDateShort(flashSale.startTime) || '' }}</td>
            <td>{{ formatDateShort(flashSale.endTime) || '' }}</td>
            <td>{{ flashSale.discountRate }}</td>
            <td>{{ flashSale.maxQuantity }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'FlashSaleView', params: { flashSaleId: flashSale.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'FlashSaleEdit', params: { flashSaleId: flashSale.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(flashSale)"
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
        <span id="mallApp.flashSale.delete.question" data-cy="flashSaleDeleteDialogHeading" v-text="t$('entity.delete.title')"></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-flashSale-heading" v-text="t$('mallApp.flashSale.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-flashSale"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeFlashSale()"
          ></button>
        </div>
      </template>
    </b-modal>
    <div v-show="flashSales && flashSales.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./flash-sale.component.ts"></script>
