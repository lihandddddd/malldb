<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="product">
        <h2 class="jh-entity-heading" data-cy="productDetailsHeading">
          <span v-text="t$('mallApp.product.detail.title')"></span> {{ product.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('mallApp.product.name')"></span>
          </dt>
          <dd>
            <span>{{ product.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.product.description')"></span>
          </dt>
          <dd>
            <span>{{ product.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.product.price')"></span>
          </dt>
          <dd>
            <span>{{ product.price }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.product.stock')"></span>
          </dt>
          <dd>
            <span>{{ product.stock }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.product.flashSale')"></span>
          </dt>
          <dd>
            <div v-if="product.flashSale">
              <router-link :to="{ name: 'FlashSaleView', params: { flashSaleId: product.flashSale.id } }">{{
                product.flashSale.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="product.id" :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-details.component.ts"></script>
