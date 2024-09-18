import { CategoryService } from '../../../../service/category.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductDTO } from '../../../../dtos/products/product.dto';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Category } from '../../../../models/category';
import { CategoryDTO } from '../../../../dtos/category/category.dto';

@Component({
  selector: 'category-update-admin',
  templateUrl: './update.category.admin.component.html',
  styleUrl: './update.category.admin.component.css',
})
export class CategoryUpdateAdminComponent {
  constructor() {}
}
