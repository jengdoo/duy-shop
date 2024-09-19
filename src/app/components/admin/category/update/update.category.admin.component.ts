import { CategoryResponse } from './../../../../response/category/category';
import { CategoryService } from '../../../../service/category.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductDTO } from '../../../../dtos/products/product.dto';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'category-update-admin',
  templateUrl: './update.category.admin.component.html',
  styleUrl: './update.category.admin.component.css',
})
export class CategoryUpdateAdminComponent {
  categoryForm: FormGroup;
  category?: CategoryResponse;
  categoryId?: number;
  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder,
    private router: ActivatedRoute,
    private snackBar: MatSnackBar,
    private route: Router
  ) {
    this.router.paramMap.subscribe((params) => {
      const categoryId = params.get('id');
      if (categoryId) {
        this.loadCategory(+categoryId);
      } else {
        console.log('Không tìm thấy danh mục với id:', categoryId);
      }
    });
    this.categoryForm = this.fb.group({
      id: [{ value: '', disabled: true }],
      name: ['', Validators.required],
    });
  }
  loadCategory(categoryId: number) {
    debugger;
    this.categoryService.getCategoryById(categoryId).subscribe({
      next: (response) => {
        debugger;
        this.categoryId = response.id;
        this.category = response;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log('errors', error);
      },
    });
  }
  onUpdate() {
    debugger;
    if (this.categoryId && this.categoryForm.valid) {
      const categoryResponse: CategoryResponse = this.categoryForm.value; // Lấy dữ liệu từ form
      this.categoryService
        .updateCategory(this.categoryId, categoryResponse)
        .subscribe({
          next: (response) => {
            debugger;
            this.snackBar.open('Cập nhật danh mục thành công!', 'Đóng', {
              duration: 2000,
            });
            this.route.navigate(['admin/categories']);
          },
          error: (err) => {
            debugger;
            console.error('Lỗi khi cập nhật danh mục:', err);
            this.snackBar.open('Cập nhật danh mục thất bại!', 'Đóng', {
              duration: 2000,
            });
          },
        });
    }
  }
}
