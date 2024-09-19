import { inject, Injectable } from '@angular/core';
import { TokenService } from '../service/token.service';
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { UserService } from '../service/user.service';
import { UserResponse } from '../response/user/user.response';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard {
  user?: UserResponse | null;
  constructor(
    private tokenService: TokenService,
    private router: Router,
    private userService: UserService
  ) {}
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const isTokenExpired = this.tokenService.isTokenExpired();
    const isUserIdValid = this.tokenService.getUserId() > 0;
    this.user = this.userService.getUserFromLocalStorage();
    const isAdmin = this.user?.roles.name == 'ADMIN';
    debugger;
    if (!isTokenExpired && isUserIdValid && isAdmin) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
export const AdminGuardFn: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  debugger;
  const authGuard = inject(AdminGuard);
  return authGuard.canActivate(route, state);
};
