import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ProductService } from './product.service'; // Cập nhật đường dẫn nếu cần
import { Product } from '../models/product'; // Cập nhật đường dẫn nếu cần
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: Map<number, number> = new Map();
  private cartSubject: BehaviorSubject<
    { product: Product; quantity: number }[]
  > = new BehaviorSubject<{ product: Product; quantity: number }[]>([]);

  constructor(
    private productService: ProductService,
    private http: HttpClient
  ) {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = new Map<number, number>(JSON.parse(storedCart));
      this.updateCartItems(); // Cập nhật các sản phẩm trong giỏ hàng ngay lập tức
    }
    this.loadCartFromLocalStorage();
  }

  addToCart(productId: number, quantity: number = 1) {
    if (this.cart.has(productId)) {
      this.cart.set(productId, this.cart.get(productId)! + quantity);
    } else {
      this.cart.set(productId, quantity);
    }
    this.saveCartLocalStorage();
    this.updateCartItems();
  }

  removeItem(productId: number) {
    this.cart.delete(productId);
    this.saveCartLocalStorage();
    this.updateCartItems();
  }

  clearCart() {
    this.cart.clear();
    this.saveCartLocalStorage();
    this.updateCartItems();
  }

  getCartItems(): Observable<{ product: Product; quantity: number }[]> {
    debugger;
    return this.cartSubject.asObservable();
  }
  getCart(): Map<number, number> {
    return this.cart;
  }
  getTotalProducts(): number {
    return this.cart.size;
  }

  getTotalQuantity(): number {
    debugger;
    let totalQuantity = 0;
    this.cart.forEach((quantity) => (totalQuantity += quantity));
    return totalQuantity;
  }

  private saveCartLocalStorage() {
    localStorage.setItem(
      'cart',
      JSON.stringify(Array.from(this.cart.entries()))
    );
  }

  private updateCartItems() {
    const productIds = Array.from(this.cart.keys());

    // Kiểm tra nếu productIds không rỗng
    if (productIds.length === 0) {
      // Nếu không có sản phẩm nào, set giỏ hàng là rỗng
      this.cartSubject.next([]);
      return;
    }

    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {
        // Map các sản phẩm với số lượng trong giỏ hàng
        const cartItems = productIds.map((productId) => {
          const product = products.find((p) => p.id === productId);
          if (product) {
            if (product.thumbnail) {
              product.thumbnail = `${environment.apiBaseUrl}/product/images/${product.thumbnail}`;
            }
            return { product, quantity: this.cart.get(productId)! };
          } else {
            // Nếu sản phẩm không tìm thấy, trả về giỏ hàng rỗng hoặc sản phẩm mặc định
            return { product: {} as Product, quantity: 0 };
          }
        });
        this.cartSubject.next(cartItems);
      },
      error: (error) => {
        console.error('Error fetching products:', error);
        this.cartSubject.next([]); // Set giỏ hàng thành rỗng trong trường hợp lỗi
      },
    });
  }

  private loadCartFromLocalStorage(): void {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    this.cart = new Map<number, number>(cart);
    this.updateCartItems(); // Cập nhật giỏ hàng sau khi tải từ localStorage
  }
}
