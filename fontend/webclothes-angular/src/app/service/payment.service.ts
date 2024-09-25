import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { Observable } from 'rxjs';
import { PaymentDTO } from '../dtos/payment/payment.dto';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {
  private apiUrl = `${environment.apiBaseUrl}/payment`;
  constructor(private http: HttpClient) {}
  createPayment(paymentRequest: {
    order_id: number;
    paymentMethod: string;
  }): Observable<PaymentDTO> {
    debugger;
    return this.http.post<PaymentDTO>(`${this.apiUrl}/create`, paymentRequest);
  }
  paymentCallback(params: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/payment-callback`, { params });
  }
}
