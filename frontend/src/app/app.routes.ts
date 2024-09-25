import { Routes } from '@angular/router';
import { LoginComponent } from './core/auth/login/login.component';
import { MainComponent } from './core/main/main.component';
import { authGuard } from './core/auth/authentication.guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: MainComponent, canMatch: [authGuard] },
];
