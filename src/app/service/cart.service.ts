import { Injectable } from '@angular/core';
import { ProductService } from './product.service';
// import {LocalStorageService} from 'ng-webstorage';
@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cart: Map<number, number> = new Map();
  constructor(private productService: ProductService) {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = new Map(JSON.parse(storedCart));
    }
  }
  addToCart(productId: number, quantity: number = 1) {
    debugger;
    if (this.cart.has(productId)) {
      // nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên
      this.cart.set(productId, this.cart.get(productId)! + quantity);
    } else {
      // nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm với số lượng
      this.cart.set(productId, quantity);
    }
    // lưu vào local storage sau khi thay đổi
    this.saveCartLocalStorage();
  }
  getCart(): Map<number, number> {
    return this.cart;
  }
  // lưu vào local storage
  private saveCartLocalStorage() {
    debugger;
    localStorage.setItem(
      'cart',
      JSON.stringify(Array.from(this.cart.entries()))
    );
  }
  clearCart() {
    this.cart.clear();
    this.saveCartLocalStorage();
  }
}
