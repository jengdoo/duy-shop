import { CategoryService } from './../../../../service/category.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../../../service/product.service';
import { ProductDTO } from '../../../../dtos/products/product.dto';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Category } from '../../../../models/category';

@Component({
  selector: 'product-create-admin',
  templateUrl: './create.product.admin.component.html',
  styleUrl: './create.product.admin.component.css',
})
export class ProductCrateAdminComponent implements OnInit {
  productForm: FormGroup;
  categories: Category[] = [];
  scannedData: string = '';
  orderId?: number;
  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private router: Router,
    private snackBar: MatSnackBar,
    private categoryService: CategoryService
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      quantity: ['', [Validators.required, Validators.min(0)]],
      description: ['', Validators.required],
      category_id: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    this.getCategories();
  }

  onSubmit() {
    debugger;
    if (this.productForm.valid) {
      debugger;
      const productDTO: ProductDTO = this.productForm.value;
      this.productService.createProduct(productDTO).subscribe({
        next: (response: any) => {
          debugger;
          console.log('Product created successfully', response);
          this.showMessage('Thêm sản phẩm thành công');
          this.productForm.reset();
          this.router.navigate(['admin/products']);
        },
        complete: () => {
          debugger;
        },
        error: (error) => {
          debugger;
          console.error('Error creating product', error);
          if (error.error && error.error.text) {
            console.error('Error response text:', error.error.text);
          }
          this.showMessage('Thêm sản phẩm thất bại!');
        },
      });
    }
  }
  onScanSuccess(result: string) {
    const productDTO = JSON.parse(result);
    this.productForm.patchValue(productDTO); // Tự động điền dữ liệu vào form

    // Gọi API thêm sản phẩm
    this.productService.createProduct(this.productForm.value).subscribe({
      next: (response: any) => {
        this.showMessage('Thêm sản phẩm thành công');
        this.productForm.reset(); // Reset form sau khi thêm
        this.router.navigate(['admin/products']);
      },
      error: (error) => {
        this.showMessage('Thêm sản phẩm thất bại!');
      },
    });
  }
  private populateForm(qrData: string) {
    try {
      const data = JSON.parse(qrData);
      this.productForm.patchValue({
        name: data.name,
        price: data.price,
        quantity: data.quantity,
        description: data.description,
        category_id: data.categoryId,
      });
    } catch (error) {
      console.error('Invalid QR code data', error);
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
  getCategories() {
    this.categoryService.getCategory().subscribe({
      next: (categories: Category[]) => {
        debugger;
        this.categories = categories;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.log('error category', error);
      },
    });
  }
}
