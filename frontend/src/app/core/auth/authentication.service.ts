import {
  HttpClient,
  HttpErrorResponse,
  HttpStatusCode,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { TokenService } from './token/token.service';
import { Authentication } from './models/authentication.model';
import { AuthenticationResponse } from './models/authentication-response.model';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private LOGIN_URL = environment.apiUrl + '/login';

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  login(username: string, password: string) {
    const authentication: Authentication = { username, password };
    return this.authenticate(authentication).pipe(
      map((response) => {
        console.log('Authenticated', response);
        this.tokenService.setToken(response.token);

        return response;
      }),
      catchError(this.handleError)
    );
  }

  private authenticate(
    authentication: Authentication
  ): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(
      this.LOGIN_URL,
      authentication
    );
  }

  private handleError(error: HttpErrorResponse): Observable<Error> {
    if (error.status === HttpStatusCode.Unauthorized) {
      console.log('Unauthorized');
      return throwError(() => new Error('Invalid username or password'));
    }
    return throwError(
      () => new Error('Something bad happened; please try again later.')
    );
  }
}
