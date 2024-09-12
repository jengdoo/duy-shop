import { TokenService } from './../../service/token.service';
import { UserService } from '../../service/user.service';

import { Component, ElementRef, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterDTO } from '../../dtos/users/register.dto';
import { LoginDTO } from '../../dtos/users/login.dto';
import { LoginResponse } from '../../response/user/login-response';
import { RoleService } from '../../service/role.service';
import { Role } from '../../models/roles';

@Component({
  selector: 'app-login',
  // standalone: true,
  // imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  @ViewChild('wrapper') wrapper!: ElementRef;
  @ViewChild('registerLink') registerLink!: ElementRef;
  @ViewChild('loginLink') loginLink!: ElementRef;
  @ViewChild('registerForm') registerForm!: NgForm;
  @ViewChild('loginForm') loginForm!: NgForm;
  ngAfterViewInit(): void {
    const wrapper = this.wrapper.nativeElement;
    const registerLink = this.registerLink.nativeElement;
    const loginLink = this.loginLink.nativeElement;

    registerLink.onclick = () => {
      wrapper.classList.add('active');
    };
    loginLink.onclick = () => {
      wrapper.classList.remove('active');
    };
  }
  fullName: string;
  phoneNumber: string;
  password: string;
  confirmPassword: string;
  address: string;
  dateOfBirth: Date;
  today = new Date();
  roles: Role[] = [];
  selectedRole: Role | undefined;
  constructor(
    private router: Router,
    private UserService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) {
    this.fullName = '';
    this.phoneNumber = '';
    this.password = '';
    this.confirmPassword = '';
    this.address = '';
    this.dateOfBirth = new Date(
      this.today.getFullYear() - 18,
      this.today.getMonth(),
      this.today.getDate()
    );
    this.phoneNumberLogin = '0312121212';
    this.passwordLogin = '123456';
  }

  ngOnInit() {
    debugger;
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {
        debugger;
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      },
      error: (error: any) => {
        debugger;
        console.error('error getting roles:', error);
      },
    });
  }

  log() {
    alert(
      `name: ${this.fullName} phone ${this.phoneNumber} pass ${this.password} cfpass ${this.confirmPassword}`
    );
  }
  register() {
    debugger;
    const registerDTO: RegisterDTO = {
      fullname: this.fullName,
      phone_number: this.phoneNumber,
      address: this.address,
      password: this.password,
      confirm_password: this.confirmPassword,
      date_of_birth: this.dateOfBirth,
      facebook_account_id: 0,
      google_account_id: 0,
      roles_id: 1,
    };
    this.UserService.register(registerDTO).subscribe({
      next: (response: any) => {
        debugger;
        this.router.navigate(['/login']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        alert(`Cannot register,error: ${error.error}`);
      },
    });
  }
  checkPasswordMatch() {
    const passwordControl = this.registerForm?.controls['password'];
    const confirmPassword = this.registerForm?.controls['confirmPassword'];

    if (passwordControl && confirmPassword) {
      if (passwordControl.value !== confirmPassword.value) {
        confirmPassword.setErrors({ passwordMismatch: true });
        console.log('ok');
      } else {
        confirmPassword.setErrors(null);
        console.log('not ok');
      }
    }
  }
  phoneNumberLogin: string;
  passwordLogin: string;

  login() {
    debugger;
    const loginDTO: LoginDTO = {
      phone_number: this.phoneNumberLogin,
      password: this.passwordLogin,
      role_id: this.selectedRole?.id ?? 1,
    };
    this.UserService.login(loginDTO).subscribe({
      next: (response: LoginResponse) => {
        debugger;
        const { token } = response;
        this.tokenService.setToken(token);
        //  this.router.navigate(['/login']);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        alert(`Cannot register,error: ${error.error}`);
      },
    });
  }
}
