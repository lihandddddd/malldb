<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="flashSale">
        <h2 class="jh-entity-heading" data-cy="flashSaleDetailsHeading">
          <span v-text="t$('mallApp.flashSale.detail.title')"></span> {{ flashSale.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('mallApp.flashSale.name')"></span>
          </dt>
          <dd>
            <span>{{ flashSale.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.flashSale.startTime')"></span>
          </dt>
          <dd>
            <span v-if="flashSale.startTime">{{ formatDateLong(flashSale.startTime) }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.flashSale.endTime')"></span>
          </dt>
          <dd>
            <span v-if="flashSale.endTime">{{ formatDateLong(flashSale.endTime) }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.flashSale.discountRate')"></span>
          </dt>
          <dd>
            <span>{{ flashSale.discountRate }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.flashSale.maxQuantity')"></span>
          </dt>
          <dd>
            <span>{{ flashSale.maxQuantity }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="flashSale.id"
          :to="{ name: 'FlashSaleEdit', params: { flashSaleId: flashSale.id } }"
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

<script lang="ts" src="./flash-sale-details.component.ts"></script>
