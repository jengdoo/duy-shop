import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../../service/order.service';
import { Product } from '../../../models/product';
import { ProductService } from '../../../service/product.service';
import { environment } from '../../../environments/environment';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'product-admin',
  templateUrl: './product.admin.component.html',
  styleUrl: './product.admin.component.css',
})
export class ProductAdminComponent implements OnInit {
  products: Product[] = [];
  currentPage: number = 0;
  itemsPePage: number = 10;
  totalPages: number = 0;
  keyword: string = '';
  visiblePages: number[] = [];
  selectedCategoryId: number = 0;
  product?: Product;
  constructor(
    private productService: ProductService,
    private snackBar: MatSnackBar
  ) {}
  ngOnInit(): void {
    this.getAdminProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPePage
    );
  }
  getAdminProducts(
    keyword: string,
    selectedCategoryId: number,
    page: number,
    limit: number
  ) {
    // this.tokenService.removeToken();
    this.productService
      .getProducts(keyword, selectedCategoryId, page, limit)
      .subscribe({
        next: (response: any) => {
          debugger;
          response.productResponseList.forEach((product: Product) => {
            product.url = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
          });
          this.products = response.productResponseList;
          this.product = response.productResponseList;
          this.totalPages = response.totalPage;
          this.visiblePages = this.generateVisiblePageArray(
            this.currentPage,
            this.totalPages
          );
        },
        error: (error: any) => {
          console.error('error fetching product:', error);
          alert(error);
        },
      });
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAdminProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.itemsPePage
    );
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
  deleteProduct(productId: number) {
    this.productService.deleteProduct(productId).subscribe({
      next: (response: any) => {
        this.showMessage('Xóa thành công!');
        this.getAdminProducts(
          this.keyword,
          this.selectedCategoryId,
          this.currentPage,
          this.itemsPePage
        );
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        this.showMessage('Xóa thất bại');
        console.log('error', error);
      },
    });
  }
  private showMessage(message: string) {
    this.snackBar.open(message, 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }
}
