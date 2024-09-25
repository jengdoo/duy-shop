import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../service/payment.service';

@Component({
  selector: 'app-payment-result',
  templateUrl: './payment-result.component.html',
  styleUrl: './payment-result.component.css',
})
export class PaymentResultComponent implements OnInit {
  paymentResult: any;

  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService,
    private router: Router
  ) {}
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      console.log('Query Params: ', params); // Kiểm tra params
      this.paymentService.paymentCallback(params).subscribe(
        (result) => {
          console.log('Payment result: ', result); // Kiểm tra kết quả
          this.paymentResult = result;
          console.log('Order ID:', this.paymentResult.orderId);
        },
        (error) => {
          console.error('Payment callback error: ', error);
          this.paymentResult = {
            status: 'FAILED',
            message: 'Có lỗi xảy ra trong quá trình xử lý',
          };
        }
      );
    });
  }
  navigateHome() {
    this.router.navigate(['/']);
  }

  viewOrder() {
    this.router.navigate([
      'orders',
      this.paymentResult.orderId, // Truyền trực tiếp orderId
    ]);
  }
}
