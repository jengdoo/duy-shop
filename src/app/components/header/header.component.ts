import { Component, OnInit } from '@angular/core';
import { CartService } from '../../service/cart.service';
import { ProductService } from '../../service/product.service';
import { Product } from '../../models/product';
import { Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { UserResponse } from '../../response/user/user.response';
import { Router } from '@angular/router';
import { TokenService } from '../../service/token.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  cartProductCount: number = 0;
  cartItems: { product: Product; quantity: number }[] = [];
  userResponse?: UserResponse | null;
  private cartSubscription: Subscription = new Subscription();

  constructor(
    private cartService: CartService,
    private userService: UserService,
    private router: Router,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.cartSubscription = this.cartService.getCartItems().subscribe({
      next: (cartItems) => {
        this.cartItems = cartItems;
        // Sử dụng getTotalQuantity để tính tổng số lượng sản phẩm
        this.cartProductCount = this.cartService.getTotalProducts();
      },
      error: (error) => {
        console.error('Error fetching cart items:', error);
      },
    });
    this.userResponse = this.userService.getUserFromLocalStorage();
  }

  ngOnDestroy(): void {
    this.cartSubscription.unsubscribe();
  }

  getVisibleCartItems(): { product: Product; quantity: number }[] {
    return this.cartItems.slice(0, 5); // Chỉ trả về 5 sản phẩm đầu tiên
  }
  logout(): void {
    // Thực hiện đăng xuất và xóa thông tin người dùng
    localStorage.removeItem('user');
    this.tokenService.removeToken();
    this.userResponse = null; // Xóa thông tin người dùng
    this.router.navigate(['/login']); // Chuyển hướng về trang chính hoặc trang đăng nhập
  }
}
