import { ProductImage } from './../../models/product.image';
import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { environment } from '../../environments/environment';
import { CartService } from '../../service/cart.service';

@Component({
  selector: 'app-detail-product',
  // standalone: true,
  // imports: [HeaderComponent, FooterComponent],
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.css',
})
export class DetailProductComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  selectedImageIndex: number = -1;
  allProducts: Product[] = [];
  productsToShow: Product[] = [];
  startIndex = 0;
  categoryId?: number;
  showTickFlag: boolean = false;
  quantity: number = 1;
  constructor(
    private productService: ProductService,
    private cartService: CartService
  ) {
    this.loadProducts();
  }
  ngOnInit() {
    const idParam = 1; // Ví dụ ID từ tham số hoặc từ URL
    if (idParam !== null) {
      this.productId = +idParam;
    }
    if (!isNaN(this.productId)) {
      this.productService.getDetailProduct(this.productId).subscribe({
        next: (response: any) => {
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiBaseUrl}/product/images/${product_image.image_url}`;
            });
          }
          this.product = response;
          if (this.product) {
            this.categoryId = this.product.categoryId;
            this.loadRelatedProducts(this.categoryId);
            this.showImage(0);
          }
        },
        error: (error: any) => {
          console.log('Lỗi khi lấy chi tiết sản phẩm', error);
        },
      });
    } else {
      console.error('productId không hợp lệ:', idParam);
    }
  }
  addToCart() {
    debugger;
    if (this.product) {
      this.cartService.addToCart(this.product.id, this.quantity);
    } else {
      console.error('khongt he mua');
    }
  }
  increaseQuantity() {
    this.quantity++;
  }
  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  buyNow() {
    // mua hang
  }
  loadRelatedProducts(categoryId?: number): void {
    if (categoryId !== undefined && categoryId !== null) {
      this.productService.getProductsByCategory(categoryId).subscribe({
        next: (products: any) => {
          if (Array.isArray(products)) {
            this.allProducts = products.filter(
              (p) => p.id !== this.product?.id
            );
            this.allProducts = this.allProducts.map((product) => {
              product.url = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
              return product;
            });
            this.loadProducts(); // Cập nhật sản phẩm hiển thị sau khi dữ liệu được tải về
          }
        },
        error: (error: any) => {
          console.log('Error fetching related products', error);
        },
      });
    }
  }
  showImage(index: number): void {
    if (
      this.product &&
      this.product.product_images &&
      this.product.product_images.length > 0
    ) {
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }
      this.currentImageIndex = index;
    }
  }
  thumbnailClick(index: number) {
    this.currentImageIndex = index;
    this.selectedImageIndex = index;
  }
  nextImage() {
    this.showImage(this.currentImageIndex + 1);
  }
  previousImage() {
    this.showImage(this.currentImageIndex - 1);
  }
  loadProducts(): void {
    const endIndex = Math.min(this.startIndex + 4, this.allProducts.length);
    this.productsToShow = this.allProducts.slice(this.startIndex, endIndex);
  }
  onNext(): void {
    if (this.startIndex + 4 < this.allProducts.length) {
      this.startIndex += 1;
    } else {
      this.startIndex = 0;
    }
    this.loadProducts(); // Cập nhật sản phẩm hiển thị
  }
  onPrev(): void {
    if (this.startIndex - 4 >= 0) {
      this.startIndex -= 1;
    } else {
      this.startIndex = Math.max(this.allProducts.length - 4, 0); // Reset về cuối danh sách nếu đầu danh sách
    }
    this.loadProducts(); // Cập nhật sản phẩm hiển thị
  }
  showTick(event: MouseEvent) {
    this.showTickFlag = true;
    const tick = document.querySelector('.tick') as HTMLElement;
    tick.style.top = event.offsetY + 'px';
    tick.style.left = event.offsetX + 'px';
  }
}
