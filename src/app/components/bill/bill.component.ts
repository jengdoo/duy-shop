import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../dtos/orders/order.dto';
import { OrderService } from '../../service/order.service';
import { TokenService } from '../../service/token.service';
import { OrderResponse } from '../../response/oder/order.response';
import { UserService } from '../../service/user.service';
import { UserResponse } from '../../response/user/user.response';
import { OrderDetail } from '../../models/oder-detail';

@Component({
  selector: 'app-bill',
  // standalone: true,
  // imports: [],
  templateUrl: './bill.component.html',
  styleUrl: './bill.component.css',
})
export class BillComponent implements OnInit {
  orderList: OrderResponse[] = [];
  userMap: Map<number, string> = new Map();
  constructor(
    private orderService: OrderService,
    private tokenService: TokenService,
    private userService: UserService
  ) {}
  ngOnInit(): void {
    this.getOrderList();
  }
  getOrderList() {
    debugger;
    const userId = this.tokenService.getUserId();
    this.orderService.getOrderListByUser(userId).subscribe({
      next: (response: any) => {
        debugger;
        this.orderList = response;
        this.loadUserName();
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('error fetching product:', error);
        alert(error);
      },
    });
  }
  loadUserName() {
    debugger;
    const userIds = Array.from(
      new Set(this.orderList.map((order) => order.user_id))
    );
    userIds.forEach((userId) => {
      this.userService.getUserById(userId).subscribe({
        next: (user: UserResponse) => {
          debugger;
          this.userMap.set(userId, user.fullname);
        },
        error: (error: any) => {
          debugger;
          console.error(`Error fetching user ${userId}:`, error);
        },
      });
    });
  }
  getUserFullName(userId: number): string {
    return this.userMap.get(userId) || 'Unknown';
  }
  getTotalQuantity(cartItems: OrderDetail[]): number {
    debugger;
    if (!cartItems || !Array.isArray(cartItems)) {
      return 0; // Trả về 0 nếu cartItems không phải là mảng hoặc là undefined
    }
    return cartItems.reduce((total, item) => total + item.numberOfProducts, 0);
  }
}
