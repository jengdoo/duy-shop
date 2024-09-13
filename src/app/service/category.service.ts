import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiCategory = `${environment.apiBaseUrl}/category`;
  constructor(private http: HttpClient) {}
  getCategory(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiCategory);
  }
}
