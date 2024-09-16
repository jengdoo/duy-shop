import { OrderDetail } from './../../models/oder-detail';
import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Product } from '../../models/product';
import { CartService } from '../../service/cart.service';
import { ProductService } from '../../service/product.service';
import { environment } from '../../environments/environment';
import { TokenService } from '../../service/token.service';
import { OrderResponse } from '../../response/oder/order.response';
import { OrderService } from '../../service/order.service';

@Component({
  selector: 'app-order-confirm',
  // standalone: true,
  // imports: [HeaderComponent, FooterComponent],
  templateUrl: './order-detail.component.html',
  styleUrl: './order-detail.component.css',
})
export class OrderConfirmComponent implements OnInit {
  cartItems: { product: Product; quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0;
  orderResponse: OrderResponse = {
    id: 0,
    user_id: 0,
    fullname: '',
    email: '',
    phone_number: '', // Đổi từ phone_number thành phoneNumber
    address: '',
    note: '',
    order_date: new Date(), // Đổi từ order-date thành orderDate
    status: '',
    total_money: 0, // Đổi từ total_money thành totalMoney
    payment_method: '', // Đổi từ payment_method thành paymentMethod
    shipping_method: '', // Đổi từ shipping_method thành shippingMethod
    shipping_address: '', // Đổi từ shipping_address thành shippingAddress
    shipping_date: new Date(),
    active: true,
    cart_items: [], // Sử dụng OrderDetail[] thay vì []
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService
  ) {}
  ngOnInit(): void {
    this.getOrderDetails();
  }
  getOrderDetails() {
    const orderId = 10;
    this.orderService.getOrderById(orderId).subscribe({
      next: (response: any) => {
        console.log('Order Details Response:', response); // Kiểm tra cấu trúc dữ liệu
        debugger;
        this.orderResponse = {
          ...this.orderResponse,
          id: response.id,
          user_id: response.user_id,
          fullname: response.fullname,
          email: response.email,
          phone_number: response.phone_number,
          address: response.address,
          note: response.note,
          order_date: new Date(response.order_date),
          status: response.status,
          total_money: response.total_money,
          payment_method: response.payment_method,
          shipping_method: response.shipping_method,
          shipping_address: response.shipping_address,
          active: response.active,
          shipping_date: new Date(response.shipping_date),
          cart_items: response.order_details.map((order_detail: any) => {
            return {
              ...order_detail,
              productId: {
                ...order_detail.productId,
                thumbnail: `${environment.apiBaseUrl}/product/images/${order_detail.productId.thumbnail}`,
              },
            };
          }),
        };
      },
      error: (error) => {
        console.error('Error fetching order details:', error);
      },
    });
  }

  calculateTotal() {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }
  applyCoupon() {
    // viet xu ly ap dung ma giam gia
  }
}
