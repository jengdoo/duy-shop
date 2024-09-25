import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { CategoryResponse } from '../response/category/category';
import { CategoryDTO } from '../dtos/category/category.dto';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiCategory = `${environment.apiBaseUrl}/category`;
  private apiCreateCategory = `${environment.apiBaseUrl}/category/add`;
  constructor(private http: HttpClient) {}
  getCategory(): Observable<Category[]> {
    debugger;
    return this.http.get<Category[]>(this.apiCategory);
  }
  getCategoryAdmin(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(this.apiCategory);
  }
  createCategory(categoryDTO: CategoryDTO): Observable<any> {
    debugger;
    return this.http.post(this.apiCreateCategory, categoryDTO);
  }
  deleteCategory(categoryId: number): Observable<any> {
    return this.http.delete(`${this.apiCategory}/delete?id=${categoryId}`);
  }
  findById(categoryId: number): Observable<any> {
    debugger;
    return this.http.get(`${this.apiCategory}/finById?id=${categoryId}`);
  }
  updateCategory(
    categoryId: number,
    categoryResponse: CategoryResponse
  ): Observable<CategoryResponse> {
    return this.http.put<CategoryResponse>(
      `${this.apiCategory}/update/${categoryId}`,
      categoryResponse
    );
  }
}
