import { Product } from './../../models/product';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FooterComponent } from '../footer/footer.component';
import { HeaderComponent } from '../header/header.component';
import { environment } from '../../environments/environment';
import { ProductService } from '../../service/product.service';
import { TokenService } from '../../service/token.service';
import { Category } from '../../models/category';
import { CategoryService } from '../../service/category.service';

@Component({
  selector: 'app-home',
  // standalone: true,
  // imports: [FooterComponent, HeaderComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {
  productResponseList: Product[] = [];
  currentPage: number = 0;
  pageSizeCurrent: number = 12;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  selectedCategoryId: number = 0;
  keyword: string = '';
  categories: Category[] = [];
  constructor(
    private productService: ProductService,
    private tokenService: TokenService,
    private categoryService: CategoryService
  ) {}
  ngOnInit(): void {
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.pageSizeCurrent
    );
    this.getCategories();
  }
  // getProducts(page: number, limit: number) {
  //   this.tokenService.removeToken();
  //   this.productService.getProducts(page, limit).subscribe({
  //     next: (response: any) => {
  //       debugger;
  //       response.productResponseList.forEach((product: Product) => {
  //         debugger;
  //         product.url = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
  //       });
  //       this.productResponseList = response.productResponseList;
  //       this.totalPages = response.totalPage;
  //       this.visiblePages = this.generateVisiblePageArray(
  //         this.currentPage,
  //         this.totalPages
  //       );
  //     },
  //     complete: () => {
  //       debugger;
  //     },
  //     error: (error: any) => {
  //       debugger;
  //       console.error('error fetching product:', error);
  //       alert(error);
  //     },
  //   });
  // }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.pageSizeCurrent
    );
  }
  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
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
  getCategories() {
    this.categoryService.getCategory().subscribe({
      next: (categories: Category[]) => {
        debugger;
        this.categories = categories;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log('error category', error);
      },
    });
  }
  searchProducts() {
    this.currentPage = 0;
    this.pageSizeCurrent = 12;
    debugger;
    this.getProducts(
      this.keyword,
      this.selectedCategoryId,
      this.currentPage,
      this.pageSizeCurrent
    );
  }
  getProducts(
    keyword: string,
    selectedCategoryId: number,
    page: number,
    limit: number
  ) {
    this.tokenService.removeToken();
    this.productService
      .getProducts(keyword, selectedCategoryId, page, limit)
      .subscribe({
        next: (response: any) => {
          debugger;
          response.productResponseList.forEach((product: Product) => {
            product.url = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
          });
          this.productResponseList = response.productResponseList;
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
}
