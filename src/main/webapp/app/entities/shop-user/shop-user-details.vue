<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="shopUser">
        <h2 class="jh-entity-heading" data-cy="shopUserDetailsHeading">
          <span v-text="t$('mallApp.shopUser.detail.title')"></span> {{ shopUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('mallApp.shopUser.username')"></span>
          </dt>
          <dd>
            <span>{{ shopUser.username }}</span>
          </dd>
          <dt>
            <span v-text="t$('mallApp.shopUser.email')"></span>
          </dt>
          <dd>
            <span>{{ shopUser.email }}</span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="shopUser.id" :to="{ name: 'ShopUserEdit', params: { shopUserId: shopUser.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./shop-user-details.component.ts"></script>
