<template>
  <div>
    <b-dropdown-item to="/flash-sale">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.flashSale')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/order">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.order')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/product">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.product')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/shop-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.shopUser')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/user-profile">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.userProfile')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
