import { Component, OnInit } from '@angular/core';
import { CategoryResponse } from '../../../response/category/category';
import { CategoryService } from '../../../service/category.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CategoryDTO } from '../../../dtos/category/category.dto';

@Component({
  selector: 'category-admin',
  templateUrl: './category.admin.component.html',
  styleUrl: './category.admin.component.css',
})
export class CategoryAdminComponent implements OnInit {
  categories: CategoryResponse[] = [];
  categoryForm: FormGroup;
  constructor(
    private categoryService: CategoryService,
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.categoryForm = this.fb.group({
      name: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.getAllCategory();
  }
  getAllCategory() {
    debugger;
    this.categoryService.getCategory().subscribe({
      next: (response: any) => {
        debugger;
        this.categories = response;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('error', error);
      },
    });
  }
  onSubmit() {
    debugger;
    if (this.categoryForm.valid) {
      debugger;
      const categoryDTO: CategoryDTO = this.categoryForm.value;
      this.categoryService.createCategory(categoryDTO).subscribe({
        next: (response: any) => {
          debugger;
          this.showMessage('Thêm danh mục thành công');
          this.getAllCategory();
          this.categoryForm.reset();
        },
        complete: () => {
          debugger;
        },
        error: (error) => {
          debugger;
          this.showMessage('Thêm category thất bại!');
        },
      });
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
