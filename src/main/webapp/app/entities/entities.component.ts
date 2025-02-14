import { defineComponent, provide } from 'vue';

import FlashSaleService from './flash-sale/flash-sale.service';
import OrderService from './order/order.service';
import ProductService from './product/product.service';
import ShopUserService from './shop-user/shop-user.service';
import UserProfileService from './user-profile/user-profile.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('flashSaleService', () => new FlashSaleService());
    provide('orderService', () => new OrderService());
    provide('productService', () => new ProductService());
    provide('shopUserService', () => new ShopUserService());
    provide('userProfileService', () => new UserProfileService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
