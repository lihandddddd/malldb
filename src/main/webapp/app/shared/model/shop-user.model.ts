export interface IShopUser {
  id?: number;
  username?: string;
  email?: string;
}

export class ShopUser implements IShopUser {
  constructor(
    public id?: number,
    public username?: string,
    public email?: string,
  ) {}
}
