import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { OrderDTO } from '../dtos/orders/order.dto';
import { Observable } from 'rxjs';
import { OrderResponse } from '../response/oder/order.response';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = `${environment.apiBaseUrl}/orders`;
  constructor(private http: HttpClient) {}
  placeOrder(orderData: OrderDTO): Observable<OrderDTO> {
    return this.http.post<OrderDTO>(this.apiUrl, orderData);
  }
  getOrderById(orderId: number): Observable<OrderDTO[]> {
    return this.http.get<OrderDTO[]>(
      `${environment.apiBaseUrl}/orders/${orderId}`
    );
  }
  getOrderListByUser(userId: number): Observable<OrderResponse> {
    debugger;
    return this.http.get<OrderResponse>(
      `${environment.apiBaseUrl}/orders/user/${userId}`
    );
  }
}
