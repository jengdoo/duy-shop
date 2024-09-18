import { Component, OnInit } from '@angular/core';
import { OrderDTO } from '../../../dtos/orders/order.dto';
import { OrderService } from '../../../service/order.service';
import { OrderResponse } from '../../../response/oder/order.response';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'order-admin',
  templateUrl: './order.admin.component.html',
  styleUrl: './order.admin.component.css',
})
export class OrderAdminComponent implements OnInit {
  orders: OrderResponse[] = [];
  currentPage: number = 0;
  itemsPePage: number = 10;
  totalPages: number = 0;
  keyword: string = '';
  visiblePages: number[] = [];
  constructor(
    private orderService: OrderService,
    private snackBar: MatSnackBar
  ) {}
  ngOnInit(): void {
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPePage);
  }
  getAllOrders(keyword: string, page: number, limit: number) {
    debugger;
    this.orderService.getAllOrders(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.orders = response.orderResponseList;
        this.totalPages = response.totalPage;
        this.visiblePages = this.generateVisiblePageArray(
          this.currentPage,
          this.totalPages
        );
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('error', error);
      },
    });
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPePage);
  }
  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    debugger;
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 0);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 0);
    }
    return new Array(endPage - startPage + 1)
      .fill(0)
      .map((_, index) => startPage + index);
  }
  updateOrderStatus(orderId: number, newStatus: string) {
    debugger;
    this.orderService.updateOrderStatus(orderId, newStatus).subscribe({
      next: () => {
        debugger;
        this.getAllOrders(this.keyword, this.currentPage, this.itemsPePage); // Refresh the order list
        this.showSuccessMessage('Cập nhật trạng thái thành công');
      },
      complete: () => {
        debugger;
      },
      error: (error) => {
        console.error('Error updating order status:', error);
        this.showSuccessMessage('Cập nhật trạng thái thất bại');
      },
    });
  }
  private showSuccessMessage(message: string) {
    this.snackBar.open(message, 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }
}
