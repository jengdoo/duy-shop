export interface PaymentDTO {
  transactionId: string; // ID giao dịch
  amount: number; // Số tiền thanh toán
  status: string; // Trạng thái thanh toán (PENDING, SUCCESS, FAILED, v.v.)
  orderId: number;
  paymentUrl: string; // URL để chuyển hướng đến trang thanh toán VNPay
}
