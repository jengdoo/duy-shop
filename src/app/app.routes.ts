import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { DetailProductComponent } from './components/detail-product/detail-product.component';
import { OrderComponent } from './components/order/order.component';
import { OrderConfirmComponent } from './components/order-details/order-detail.component';
import { NgModule } from '@angular/core';
import { BillComponent } from './components/bill/bill.component';
import { AuthGuard, AuthGuardFn } from './auth/auth.guard';
import { AdminComponent } from './components/admin/admin.component';
import { AdminGuardFn } from './auth/auth.guard.admin';
import { OrderAdminComponent } from './components/admin/order/order.admin.component';
import { ProductAdminComponent } from './components/admin/product/product.admin.component';
import { CategoryAdminComponent } from './components/admin/category/category.admin.component';
import { ProductCrateAdminComponent } from './components/admin/product/create/create.product.admin.component';
import { CategoryUpdateAdminComponent } from './components/admin/category/update/update.category.admin.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'product/:id', component: DetailProductComponent },
  { path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn] },
  {
    path: 'orders/:id',
    component: OrderConfirmComponent,
    canActivate: [AuthGuardFn],
  },
  { path: 'bills', component: BillComponent, canActivate: [AuthGuardFn] },
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuardFn],
    children: [
      {
        path: 'orders',
        component: OrderAdminComponent,
        canActivate: [AdminGuardFn],
      },
      {
        path: 'products',
        component: ProductAdminComponent,
        canActivate: [AdminGuardFn],
      },
      {
        path: 'categories',
        component: CategoryAdminComponent,
        canActivate: [AdminGuardFn],
      },
      {
        path: 'products/create',
        component: ProductCrateAdminComponent,
        canActivate: [AdminGuardFn],
      },
      {
        path: 'category/update',
        component: CategoryUpdateAdminComponent,
        canActivate: [AdminGuardFn],
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRouterModule {}
