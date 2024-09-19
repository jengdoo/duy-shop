import { OrderDetail } from '../../models/oder-detail';

export interface OrderResponse {
  id: number;
  user_id: number;
  fullname: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  order_date: Date;
  status: string;
  total_money: number;
  payment_method: string;
  shipping_method: string;
  shipping_address: string;
  shipping_date: Date;
  active: boolean;
  cart_items: OrderDetail[]; // Define the type of items in the array
}
