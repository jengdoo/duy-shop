export interface OrderStatus {
  PENDING: string;
  PROCESSING: string;
  SHIPPED: string;
  DELIVERED: string;
  CANCELLED: string;
}
export const OrderStatusValues: OrderStatus = {
  PENDING: 'PENDING',
  PROCESSING: 'PROCESSING',
  SHIPPED: 'SHIPPED',
  DELIVERED: 'DELIVERED',
  CANCELLED: 'CANCELLED',
};
