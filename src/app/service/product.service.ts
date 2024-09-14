import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiGetProduct = `${environment.apiBaseUrl}/product`;
  constructor(private http: HttpClient) {}
  getProducts(
    keyword: string,
    selectedCategoryId: number,
    page: number,
    pageSize: number
  ): Observable<Product[]> {
    const params = new HttpParams()
      .set('name', keyword.toString())
      .set('category_id', selectedCategoryId.toString())
      .set('page', page.toString())
      .set('pageSize', pageSize.toString());
    return this.http.get<Product[]>(this.apiGetProduct, { params });
  }
  getDetailProduct(productId: number) {
    return this.http.get(
      `${environment.apiBaseUrl}/product/productId/${productId}`
    );
  }
  getProductsByCategory(categoryId: number) {
    return this.http.get(
      `${environment.apiBaseUrl}/product/categoryId/${categoryId}`
    );
  }
}
