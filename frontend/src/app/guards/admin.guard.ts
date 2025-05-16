import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const username = (localStorage.getItem('username') || '[]');

  if (username.includes('administrador')) {
    return true;
  } else {
    router.navigate(['/unauthorized']);
    return false;
  }
}