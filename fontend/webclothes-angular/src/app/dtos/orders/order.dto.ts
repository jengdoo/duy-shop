export class OrderDTO {
  id: number;
  user_id: number;
  fullname: string;
  email: string;
  phone_number: string;
  address: string;
  note: string;
  total_money: number;
  payment_method: string;
  shipping_method: string;
  // coupon_code: string;
  cart_items: { product_id: number; quantity: number }[];

  constructor(data: any) {
    this.id = data.id || 0;
    this.user_id = data.user_id || 0;
    this.fullname = data.fullname || '';
    this.email = data.email || '';
    this.phone_number = data.phone_number || '';
    this.address = data.address || '';
    this.note = data.note || '';
    this.total_money = data.total_money || 0;
    this.payment_method = data.payment_method || '';
    this.shipping_method = data.shipping_method || '';
    this.cart_items = data.cart_items || [];
  }
}
