import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OrderDTO } from '../dtos/orders/order.dto';
import { Observable } from 'rxjs';
import { OrderResponse } from '../response/oder/order.response';
import { OrderStatusValues } from '../models/order-status';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = `${environment.apiBaseUrl}/orders`;
  private apiGetAllOrders = `${environment.apiBaseUrl}/orders/get-orders-by-keyword`;
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
  getAllOrders(
    keyword: string,
    page: number,
    limit: number
  ): Observable<OrderResponse[]> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page)
      .set('limit', limit);
    return this.http.get<any>(this.apiGetAllOrders, { params });
  }
  updateOrderStatus(orderId: number, status: string): Observable<any> {
    return this.http.put(
      `${environment.apiBaseUrl}/orders/status/${orderId}`,
      status,
      {
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );
  }
}
