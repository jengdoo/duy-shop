import { CategoryService } from '../../../../service/category.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductDTO } from '../../../../dtos/products/product.dto';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Category } from '../../../../models/category';
import { CategoryDTO } from '../../../../dtos/category/category.dto';
import { CategoryResponse } from '../../../../response/category/category';

@Component({
  selector: 'category-update-admin',
  templateUrl: './update.category.admin.component.html',
  styleUrl: './update.category.admin.component.css',
})
export class CategoryUpdateAdminComponent {
  categoryForm: FormGroup;
  category?: Category;
  categoryId?: number;
  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {
    this.route.params.subscribe((params) => {
      const orderId = +params['id']; // Lấy ID từ URL
      this.getCategoryId(orderId); // Gọi hàm với ID
    });
    this.categoryForm = this.fb.group({
      id: [{ value: '', disabled: true }],
      name: ['', Validators.required],
    });
  }
  getCategoryId(categoryId: number) {
    this.categoryService.findById(categoryId).subscribe({
      next: (response: any) => {
        this.category = response;
        this.categoryId = response.id;
      },
      error: (error: any) => {
        console.error('Error:', error);
      },
    });
  }
  updateCategory() {
    if (this.categoryId && this.categoryForm.valid) {
      const categoryResponse: CategoryResponse = this.categoryForm.value;
      this.categoryService
        .updateCategory(this.categoryId, categoryResponse)
        .subscribe({
          next: (response) => {
            this.showMessage('Cập nhật thành công');
            this.router.navigate(['admin/categories']);
          },
          error: (error) => {
            this.showMessage('Cập nhật thất bại');
          },
        });
    } else {
      this.showMessage('Form or id không chính xác');
    }
  }
  private showMessage(message: string) {
    this.snackBar.open(message, 'Đóng', {
      duration: 3000, // Thời gian hiển thị thông báo (3 giây)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['success-snackbar'],
    });
  }
}
