export interface IFlashSale {
  id?: number;
  name?: string;
  startTime?: Date;
  endTime?: Date;
  discountRate?: number;
  maxQuantity?: number;
}

export class FlashSale implements IFlashSale {
  constructor(
    public id?: number,
    public name?: string,
    public startTime?: Date,
    public endTime?: Date,
    public discountRate?: number,
    public maxQuantity?: number,
  ) {}
}
