import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OrderComponent } from './components/order/order.component';
import { OrderConfirmComponent } from './components/order-details/order-detail.component';
import { NgModule } from '@angular/core';
import { BillComponent } from './components/bill/bill.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'product/:id', component: DetailProductComponent },
  { path: 'orders', component: OrderComponent },
  { path: 'orders/:id', component: OrderConfirmComponent },
  { path: 'bills', component: BillComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRouterModule {}
