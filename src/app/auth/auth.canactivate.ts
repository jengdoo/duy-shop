import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../service/token.service';
import { AuthGuard } from './auth.guard';

export const AuthGuardFn: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {
  const authGuard = inject(AuthGuard);
  return authGuard.canActivate(route, state);
};
