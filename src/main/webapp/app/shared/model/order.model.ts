import { type IProduct } from '@/shared/model/product.model';
import { type IShopUser } from '@/shared/model/shop-user.model';

import { type OrderStatus } from '@/shared/model/enumerations/order-status.model';
export interface IOrder {
  id?: number;
  orderDate?: Date;
  totalAmount?: number;
  status?: keyof typeof OrderStatus;
  product?: IProduct | null;
  shopUser?: IShopUser | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public orderDate?: Date,
    public totalAmount?: number,
    public status?: keyof typeof OrderStatus,
    public product?: IProduct | null,
    public shopUser?: IShopUser | null,
  ) {}
}
