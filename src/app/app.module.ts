import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // Thêm nếu cần
import { RouterModule, Routes } from '@angular/router'; // Thêm vào đây

import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component'; // Các thành phần khác nếu cần
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { OrderComponent } from './components/order/order.component';
import { OrderConfirmComponent } from './components/order-confirm/order-confirm.component';
import { TokenInterceptor } from './interceptors/token.interceptor';

// Định nghĩa các route
const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'home', component: HomeComponent },
  { path: 'detail-product', component: DetailProductComponent },
];

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    HomeComponent,
    DetailProductComponent,
    OrderComponent,
    OrderConfirmComponent,
    // Các thành phần khác nếu cần
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule, // Nếu cần
    // RouterModule.forRoot(routes), // Cấu hình routing
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [LoginComponent], // Thành phần khởi động chính của ứng dụng
})
export class AppModule {}
