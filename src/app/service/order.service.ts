import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { OrderDTO } from '../dtos/orders/order.dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = `${environment.apiBaseUrl}/orders`;
  constructor(private http: HttpClient) {}
  placeOrder(orderData: OrderDTO) {
    return this.http.post(this.apiUrl, orderData);
  }
  getOrderById(orderId: number): Observable<OrderDTO[]> {
    return this.http.get<OrderDTO[]>(
      `${environment.apiBaseUrl}/orders/${orderId}`
    );
  }
}
