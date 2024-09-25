import { Injectable } from '@angular/core';
import { Token } from './token.model';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  setToken(token: Token) {
    localStorage.setItem('tokenSubject', token.subject);
    localStorage.setItem('tokenExpiresAt', token.expiresAt.toString());
    localStorage.setItem('token', token.value);
  }

  getToken(): Token {
    return {
      subject: localStorage.getItem('tokenSubject') as string,
      expiresAt: Number(localStorage.getItem('tokenExpiresAt')),
      value: localStorage.getItem('token') as string,
    };
  }

  clearToken() {
    localStorage.removeItem('tokenSubject');
    localStorage.removeItem('tokenExpiresAt');
    localStorage.removeItem('token');
  }

  isTokenValid(): boolean {
    const token = this.getToken();
    return token.expiresAt > Date.now();
  }
}
