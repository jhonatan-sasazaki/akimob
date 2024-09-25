import {
  HttpClient,
  HttpErrorResponse,
  HttpStatusCode,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Authentication } from './models/authentication.model';
import { AuthenticationResponse } from './models/authentication-response.model';

const UNAUTHORIZED_MESSAGE = $localize`:@@authentication.unauthorized:Invalid username or password.`;
const GENERIC_ERROR_MESSAGE = $localize`:@@authentication.error:Something bad happened. Please try again later.`;

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private LOGIN_URL = environment.apiUrl + '/login';

  constructor(private http: HttpClient) {}

  authenticate(
    authentication: Authentication
  ): Observable<AuthenticationResponse> {
    return this.http
      .post<AuthenticationResponse>(this.LOGIN_URL, authentication)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    if (error.status === HttpStatusCode.Unauthorized) {
      console.log('Unauthorized');

      return throwError(() => new Error(UNAUTHORIZED_MESSAGE));
    }
    return throwError(() => new Error(GENERIC_ERROR_MESSAGE));
  }
}
