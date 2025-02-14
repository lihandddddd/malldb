import { type IShopUser } from '@/shared/model/shop-user.model';

export interface IUserProfile {
  id?: number;
  phoneNumber?: string | null;
  address?: string | null;
  shopUser?: IShopUser | null;
}

export class UserProfile implements IUserProfile {
  constructor(
    public id?: number,
    public phoneNumber?: string | null,
    public address?: string | null,
    public shopUser?: IShopUser | null,
  ) {}
}
