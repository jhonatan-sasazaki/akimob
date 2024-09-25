import { Injectable } from '@angular/core';
import { User } from './user.model';
import { TokenService } from '../token/token.service';
import { AuthenticationService } from '../authentication.service';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { Authentication } from '../models/authentication.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  user: User | null = null;

  constructor(
    private authenticationService: AuthenticationService,
    private tokenService: TokenService,
    private router: Router
  ) {}

  login(username: string, password: string) {
    const authentication: Authentication = { username, password };
    return this.authenticationService.authenticate(authentication).pipe(
      tap((response) => {
        this.user = {
          username: response.token.subject,
          token: response.token,
        };
        this.tokenService.setToken(this.user.token);
      })
    );
  }

  autoLogin() {
    const token = this.tokenService.getToken();
    if (this.tokenService.isTokenValid(token)) {
      this.user = {
        username: token.subject,
        token,
      };
    } else {
      this.tokenService.clearToken();
    }
  }

  logout() {
    this.user = null;
    this.tokenService.clearToken();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.user && this.tokenService.isTokenValid(this.user.token);
  }
}
