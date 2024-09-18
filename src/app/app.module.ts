import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms'; // Thêm nếu cần

import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component'; // Các thành phần khác nếu cần
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { OrderComponent } from './components/order/order.component';
import { OrderConfirmComponent } from './components/order-details/order-detail.component';
import { TokenInterceptor } from './interceptors/token.interceptor';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AppComponent } from './app/app.component';
import { AppRouterModule } from './app.routes';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { BillComponent } from './components/bill/bill.component';
import { AdminComponent } from './components/admin/admin.component';
import { OrderAdminComponent } from './components/admin/order/order.admin.component';
import { ProductAdminComponent } from './components/admin/product/product.admin.component';
import { CategoryAdminComponent } from './components/admin/category/category.admin.component';
import { ProductCrateAdminComponent } from './components/admin/product/create/create.product.admin.component';
import { CategoryUpdateAdminComponent } from './components/admin/category/update/update.category.admin.component';
// Định nghĩa các route
// const routes: Routes = [
//   { path: '', component: LoginComponent },
//   { path: 'home', component: HomeComponent },
//   { path: 'detail-product', component: DetailProductComponent },
// ];

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    LoginComponent,
    HomeComponent,
    DetailProductComponent,
    OrderComponent,
    OrderConfirmComponent,
    AppComponent,
    ConfirmDialogComponent,
    BillComponent,
    OrderAdminComponent,
    ProductAdminComponent,
    AdminComponent,
    CategoryAdminComponent,
    ProductCrateAdminComponent,
    CategoryUpdateAdminComponent,
    // Các thành phần khác nếu cần
  ],
  imports: [
    MatSnackBarModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRouterModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    provideAnimationsAsync(),
  ],
  bootstrap: [AppComponent], // Thành phần khởi động chính của ứng dụng
})
export class AppModule {}
