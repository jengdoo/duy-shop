import { Component, HostListener, OnInit } from '@angular/core';
import { TokenService } from '../../service/token.service';
import { Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { UserResponse } from '../../response/user/user.response';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css',
})
export class AdminComponent implements OnInit {
  isSidebarCollapsed = false;
  userResponse?: UserResponse | null;
  constructor(
    private tokenService: TokenService,
    private router: Router,
    private userService: UserService
  ) {}
  ngOnInit(): void {
    this.userResponse = this.userService.getUserFromLocalStorage();
  }
  toggleSidebar() {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }
  logout() {
    localStorage.removeItem('user');
    this.tokenService.removeToken();
    this.userResponse = null;
    this.router.navigate(['/login']);
  }
  @HostListener('window:scroll', ['$event'])
  onScroll(event: Event): void {
    const scrollLeft =
      window.pageXOffset || document.documentElement.scrollLeft;
    const navbar = document.getElementById('icon-bar');
    if (navbar) {
      if (scrollLeft >= 0) {
        navbar.style.right = '0';
      } else {
        navbar.style.right = '-50px';
      }
    }
  }
}
