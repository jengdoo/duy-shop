import { Product } from './product';

export interface OrderDetail {
  id: number;
  orderId: number;
  productId: Product;
  numberOfProducts: number;
  price: number;
  totalMoney: number;
  color: '';
}
