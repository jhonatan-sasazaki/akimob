import { inject } from '@angular/core';
import { CanMatchFn, Router } from '@angular/router';
import { UserService } from './user/user.service';

export const authGuard: CanMatchFn = () => {
  return (
    inject(UserService).isLoggedIn() || inject(Router).navigate(['/login'])
  );
};
