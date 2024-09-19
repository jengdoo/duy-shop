import { ProductImage } from './../../models/product.image';
import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { Product } from '../../models/product';
import { ProductService } from '../../service/product.service';
import { environment } from '../../environments/environment';
import { CartService } from '../../service/cart.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-detail-product',
  // standalone: true,
  // imports: [HeaderComponent, FooterComponent],
  templateUrl: './detail-product.component.html',
  styleUrl: './detail-product.component.css',
})
export class DetailProductComponent implements OnInit {
  product?: Product;
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
    private cartService: CartService,
    private router: ActivatedRoute,
    private route: Router,
    private snackBar: MatSnackBar
  ) {
    this.loadProducts();
  }

  ngOnInit() {
    this.router.paramMap.subscribe((params) => {
      const idParam = params.get('id');
      if (idParam) {
        const productId = +idParam;
        this.loadProductDetails(productId);
      } else {
        console.error('productId không hợp lệ:', idParam);
      }
    });
  }

  private loadProductDetails(productId: number) {
    this.productService.getDetailProduct(productId).subscribe({
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
  }

  addToCart() {
    if (this.product) {
      if (this.product.quantity >= 1) {
        this.cartService.addToCart(this.product.id, this.quantity);
        this.showSuccessMessage('Sản phẩm đã được thêm vào giỏ hàng!');
      } else {
        this.showSuccessMessage('Sản phẩm đã hết hàng hoặc số lượng không đủ!');
      }
    } else {
      this.showSuccessMessage('Sản phẩm không hợp lệ để thêm vào giỏ hàng.');
    }
  }
  buyNow(): void {
    if (this.product) {
      if (this.product.quantity >= 1) {
        this.cartService.addToCart(this.product.id, this.quantity);
        // Chuyển hướng đến trang đơn hàng
        this.route.navigate(['/orders']); // Thay đổi đường dẫn nếu cần
      } else {
        this.showSuccessMessage('Sản phẩm đã hết hàng hoặc số lượng không đủ!');
      }
    } else {
      this.showSuccessMessage('Product không hợp lệ');
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
          console.log('Lỗi khi lấy sản phẩm liên quan', error);
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
      this.startIndex += 4;
    } else {
      this.startIndex = 0;
    }
    this.loadProducts(); // Cập nhật sản phẩm hiển thị
  }

  onPrev(): void {
    if (this.startIndex - 4 >= 0) {
      this.startIndex -= 4;
    } else {
      this.startIndex = Math.max(this.allProducts.length - 4, 0); // Reset về cuối danh sách nếu đầu danh sách
    }
    this.loadProducts(); // Cập nhật sản phẩm hiển thị
  }

  showTick(event: MouseEvent) {
    this.showTickFlag = true;
    const tick = document.querySelector('.tick') as HTMLElement;
    if (tick) {
      tick.style.top = event.offsetY + 'px';
      tick.style.left = event.offsetX + 'px';
    }
  }

  private showSuccessMessage(message: string) {
    this.snackBar.open(message, 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }
  onProductClick(productId: number) {
    debugger;
    this.route.navigate(['/product', productId]);
  }
}
