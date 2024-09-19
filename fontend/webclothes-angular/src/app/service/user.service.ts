import { UserResponse } from './../response/user/user.response';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/users/register.dto';
import { LoginDTO } from '../dtos/users/login.dto';
import { environment } from '../environments/environment';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiUserDetails = `${environment.apiBaseUrl}/users/details`;
  constructor(private http: HttpClient) {}
  private apiConfig = {
    headers: this.createHeaders(),
  };
  private createHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi',
    });
  }
  register(registerDTO: RegisterDTO): Observable<any> {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);
  }
  login(loginDTO: LoginDTO): Observable<any> {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }
  getUserById(userId: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(
      `${environment.apiBaseUrl}/users/${userId}`
    );
  }
  getUserDetail(token: string) {
    return this.http.post(this.apiUserDetails, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      }),
    });
  }
  saveUserToLocalStorage(userResponse: UserResponse) {
    try {
      const usrResponseJson = JSON.stringify(userResponse);
      localStorage.setItem('user', usrResponseJson);
    } catch (error) {
      console.log('error save user to local storage:', error);
    }
  }
  // getUserFromLocalStorage(): UserResponse | null {
  //   try {
  //     debugger;
  //     const token = localStorage.getItem('access_token'); // Điều chỉnh khóa nếu cần
  //     if (token) {
  //       // Giải mã token
  //       const decodedToken: any = jwtDecode(token);
  //       return {
  //         id: decodedToken.id ?? 0,
  //         fullname: decodedToken.fullname ?? 'Unknown',
  //         address: decodedToken.address ?? 'Unknown',
  //         phone_number: decodedToken.phone_number ?? 'Unknown',
  //         date_of_birth: decodedToken.date_of_birth,
  //         facebook_account_id: decodedToken.facebook_account_id,
  //         google_account_id: decodedToken.google_account_id,
  //         is_active: decodedToken.is_active,
  //         roles: decodedToken.roles,
  //       } as UserResponse;
  //     }
  //     return null;
  //   } catch (error) {
  //     console.error('Lỗi khi giải mã token từ local storage:', error);
  //     return null;
  //   }
  // }
  getUserFromLocalStorage(): UserResponse | null {
    try {
      debugger;
      const userResponseJson = localStorage.getItem('user');
      if (userResponseJson == null || userResponseJson == undefined) {
        return null;
      }
      const user = JSON.parse(userResponseJson);
      return user;
    } catch (error) {
      debugger;
      console.log('error', error);
      return null;
    }
  }
}
