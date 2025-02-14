import { Authority } from '@/shared/security/authority';
const Entities = () => import('@/entities/entities.vue');

const FlashSale = () => import('@/entities/flash-sale/flash-sale.vue');
const FlashSaleUpdate = () => import('@/entities/flash-sale/flash-sale-update.vue');
const FlashSaleDetails = () => import('@/entities/flash-sale/flash-sale-details.vue');

const Order = () => import('@/entities/order/order.vue');
const OrderUpdate = () => import('@/entities/order/order-update.vue');
const OrderDetails = () => import('@/entities/order/order-details.vue');

const Product = () => import('@/entities/product/product.vue');
const ProductUpdate = () => import('@/entities/product/product-update.vue');
const ProductDetails = () => import('@/entities/product/product-details.vue');

const ShopUser = () => import('@/entities/shop-user/shop-user.vue');
const ShopUserUpdate = () => import('@/entities/shop-user/shop-user-update.vue');
const ShopUserDetails = () => import('@/entities/shop-user/shop-user-details.vue');

const UserProfile = () => import('@/entities/user-profile/user-profile.vue');
const UserProfileUpdate = () => import('@/entities/user-profile/user-profile-update.vue');
const UserProfileDetails = () => import('@/entities/user-profile/user-profile-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'flash-sale',
      name: 'FlashSale',
      component: FlashSale,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flash-sale/new',
      name: 'FlashSaleCreate',
      component: FlashSaleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flash-sale/:flashSaleId/edit',
      name: 'FlashSaleEdit',
      component: FlashSaleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'flash-sale/:flashSaleId/view',
      name: 'FlashSaleView',
      component: FlashSaleDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order',
      name: 'Order',
      component: Order,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/new',
      name: 'OrderCreate',
      component: OrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/:orderId/edit',
      name: 'OrderEdit',
      component: OrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/:orderId/view',
      name: 'OrderView',
      component: OrderDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product',
      name: 'Product',
      component: Product,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/new',
      name: 'ProductCreate',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/edit',
      name: 'ProductEdit',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/view',
      name: 'ProductView',
      component: ProductDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'shop-user',
      name: 'ShopUser',
      component: ShopUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'shop-user/new',
      name: 'ShopUserCreate',
      component: ShopUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'shop-user/:shopUserId/edit',
      name: 'ShopUserEdit',
      component: ShopUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'shop-user/:shopUserId/view',
      name: 'ShopUserView',
      component: ShopUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-profile',
      name: 'UserProfile',
      component: UserProfile,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-profile/new',
      name: 'UserProfileCreate',
      component: UserProfileUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-profile/:userProfileId/edit',
      name: 'UserProfileEdit',
      component: UserProfileUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'user-profile/:userProfileId/view',
      name: 'UserProfileView',
      component: UserProfileDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
