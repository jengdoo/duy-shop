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

  orderData: OrderDTO = {
    user_id: 3,
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: [],
  };
  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private fb: FormBuilder,
    private orderService: OrderService,
    private snackBar: MatSnackBar
  ) {
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],
      email: ['', [Validators.email]],
      phone_number: ['', [Validators.required, Validators.minLength(6)]],
      address: ['', [Validators.required, Validators.minLength(5)]],
      payment_method: 'cod',
      shipping_method: 'express',
      note: '',
    });
  }
  ngOnInit(): void {
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        this.cartItems = productIds.map((productId) => {
          const product = products.find((p) => p.id === productId);
          if (product) {
            if (product.thumbnail) {
              product.thumbnail = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
            }
          }
          return {
            product: product!,
            quantity: cart.get(productId)!,
          };
        });
        console.log('haha');
      },
      complete: () => {
        this.calculateTotal();
      },
      error: (error: any) => {
        console.log('erorr:', error);
      },
    });
  }
  calculateTotal(): number {
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + item.product.price * item.quantity,
      0
    );
    return this.totalAmount; // Optional: Return the total amount if needed
  }
  placeOrder() {
    debugger;
    if (this.orderForm.valid) {
      debugger;
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value,
      };
      this.orderData.cart_items = this.cartItems.map((cartItem) => ({
        product_id: cartItem.product.id,
        quantity: cartItem.quantity,
      }));
      this.orderData.total_money = this.calculateTotal();
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response) => {
          console.log('Đặt hàng thành công');
          this.showSuccessMessage();
        },
        error: (error) => {
          console.error('Đặt hàng thất bại', error);
          this.showErrorMessage();
        },
      });
    } else {
      // Hiển thị thông báo lỗi nếu form không hợp lệ
      this.showErrorMessage();
    }
  }
  showSuccessMessage() {
    this.snackBar.open('Đặt hàng thành công!', 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }

  showErrorMessage() {
    this.snackBar.open('Đặt hàng thất bại. Vui lòng thử lại.', 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['error-snackbar'],
    });
  }
}
