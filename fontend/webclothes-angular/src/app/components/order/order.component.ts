import { Component, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { Product } from '../../models/product';
import { CartService } from '../../service/cart.service';
import { ProductService } from '../../service/product.service';
import { environment } from '../../environments/environment';
import { OrderDTO } from '../../dtos/orders/order.dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Validator } from 'class-validator';
import { OrderService } from '../../service/order.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component';
import { Route, Router } from '@angular/router';
import { TokenService } from '../../service/token.service';
import { OrderResponse } from '../../response/oder/order.response';
import { PaymentService } from '../../service/payment.service';
import { PaymentDTO } from '../../dtos/payment/payment.dto';

@Component({
  selector: 'app-order',
  // standalone: true,
  // imports: [FooterComponent, HeaderComponent],
  templateUrl: './order.component.html',
  styleUrl: './order.component.css',
})
export class OrderComponent implements OnInit {
  orderForm: FormGroup;
  cartItems: { product: Product; quantity: number }[] = [];
  couponCode: string = '';
  totalAmount: number = 0;
  oderId: number = 0;
  paymentDTO: PaymentDTO | undefined;
  orderData: OrderDTO = {
    id: 0,
    user_id: 0,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    // coupon_code: '',
    cart_items: [],
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private fb: FormBuilder,
    private orderService: OrderService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private router: Router,
    private tokenService: TokenService,
    private paymentService: PaymentService
  ) {
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.email]],
      phone_number: ['', [Validators.required, Validators.minLength(6)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      payment_method: ['cod'],
      shipping_method: ['express'],
      note: [''],
    });
  }

  ngOnInit(): void {
    this.loadCart();
  }
  confirmRemoveItem(index: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.removeItem(index);
      }
    });
  }
  loadCart(): void {
    debugger;
    this.orderData.user_id = this.tokenService.getUserId();

    // Lấy giỏ hàng từ CartService
    const cart = this.cartService.getCart(); // Lấy giỏ hàng dưới dạng Map

    // Lấy các productId từ giỏ hàng
    const productIds = Array.from(cart.keys());

    // Lấy thông tin sản phẩm từ API
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        debugger;
        console.log('Products:', products); // Kiểm tra dữ liệu sản phẩm ở đây

        // Ánh xạ các sản phẩm với giỏ hàng
        this.cartItems = Array.from(cart.entries()).map(
          ([productId, quantity]) => {
            const product = products.find((p) => p.id === productId);
            if (product) {
              if (product.thumbnail) {
                debugger;
                product.thumbnail = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
              }
              return {
                product: product,
                quantity: quantity,
              };
            } else {
              return { product: {} as Product, quantity: 0 };
            }
          }
        );

        // Tính tổng tiền của giỏ hàng
        this.calculateTotal();
      },
      error: (error: any) => {
        console.error('Error fetching products:', error);
      },
    });
  }
  calculateTotal(): void {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
  }

  increaseQuantity(index: number): void {
    const productId = this.cartItems[index].product.id;
    this.cartService.addToCart(productId, 1);
    this.cartItems[index].quantity += 1;
    this.calculateTotal();
  }

  decreaseQuantity(index: number): void {
    const productId = this.cartItems[index].product.id;
    if (this.cartItems[index].quantity > 1) {
      this.cartService.addToCart(productId, -1);
      this.cartItems[index].quantity -= 1;
      this.calculateTotal();
    }
  }

  removeItem(index: number): void {
    const productId = this.cartItems[index].product.id;
    this.cartService.removeItem(productId);
    this.cartItems.splice(index, 1);
    this.calculateTotal();
  }

  applyCoupon(): void {
    // Xử lý logic áp dụng coupon code
    console.log('Applying coupon:', this.couponCode);
  }

  placeOrder(): void {
    if (this.orderForm.valid) {
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value,
        cart_items: this.cartItems.map((cartItem) => ({
          product_id: cartItem.product.id,
          quantity: cartItem.quantity,
        })),
        total_money: this.totalAmount,
      };
      // Gọi API để tạo đơn hàng trước
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response: any) => {
          debugger;
          this.oderId = response.order.id;
          // Kiểm tra phương thức thanh toán
          if (this.orderData.payment_method === 'cod') {
            debugger;
            this.showSuccessMessage();
            this.clearCart();
            this.router.navigate(['/']);
          } else {
            if (response.order.id && response.order.id > 0) {
              debugger;
              this.paymentService
                .createPayment({
                  order_id: response.order.id,
                  paymentMethod: this.orderData.payment_method,
                })
                .subscribe({
                  next: (response: PaymentDTO) => {
                    debugger;
                    window.location.href = response.paymentUrl;
                  },
                  error: (error) => {
                    debugger;
                    console.error('Error during payment:', error);
                    this.showErrorMessage(
                      'Lỗi trong quá trình thanh toán. Vui lòng thử lại.'
                    );
                  },
                });
            } else {
              this.showErrorMessage('ID đơn hàng không hợp lệ.');
            }
          }
        },
        error: (error) => {
          this.showErrorMessage('Đặt hàng thất bại. Vui lòng thử lại.');
        },
      });
    } else {
      this.showErrorMessage(
        'Thông tin đặt hàng không hợp lệ. Vui lòng kiểm tra lại.'
      );
    }
  }

  showSuccessMessage(): void {
    this.snackBar.open('Đặt hàng thành công!', 'Đóng', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }

  showErrorMessage(
    message: string = 'Đặt hàng thất bại. Vui lòng thử lại.'
  ): void {
    this.snackBar.open(message, 'Đóng', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar'],
    });
  }

  clearCart(): void {
    this.cartService.clearCart();
    this.cartItems = [];
    this.totalAmount = 0;
  }
}
