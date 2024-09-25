import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from './user/user.service';

export const authenticationInterceptor: HttpInterceptorFn = (req, next) => {
  const userService = inject(UserService);

  if (userService.isLoggedIn()) {
    const token = userService.user!.token;

    const newReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token.value}`),
    });

    return next(newReq);
  }

  return next(req);
};
