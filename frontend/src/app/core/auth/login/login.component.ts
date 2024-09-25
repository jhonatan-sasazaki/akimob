import { Component, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NgIf } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { FormBuilder, Validators } from '@angular/forms';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { merge } from 'rxjs';
import { Router } from '@angular/router';
import { UserService } from '../user/user.service';

const PASSWORD_REQUIRED_MESSAGE = $localize`:@@login.password.required:Password is required`;
const PASSWORD_MIN_LENGTH_MESSAGE = $localize`:@@login.password.minLength:Password must be at least 8 characters long`;

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    NgIf,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(8)]],
  });
  passwordErrorMessage = signal(PASSWORD_REQUIRED_MESSAGE);

  constructor(
    private formBuilder: FormBuilder,
    private userService: UserService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    merge(this.password.valueChanges, this.password.statusChanges)
      .pipe(takeUntilDestroyed())
      .subscribe(() => {
        this.updatePasswordErrorMessage();
      });
  }

  get username() {
    return this.loginForm.controls.username;
  }

  get password() {
    return this.loginForm.controls.password;
  }

  updatePasswordErrorMessage() {
    if (this.password.invalid) {
      this.passwordErrorMessage.set(PASSWORD_REQUIRED_MESSAGE);
    }
    if (this.password.hasError('minlength')) {
      this.passwordErrorMessage.set(PASSWORD_MIN_LENGTH_MESSAGE);
    }
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.userService
        .login(this.username.value!, this.password.value!)
        .subscribe({
          next: () => {
            this.router.navigate(['/']);
          },
          error: (error: Error) => {
            this.displayError(error.message);
          },
        });
    }
  }

  displayError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      verticalPosition: 'top',
    });
  }
}
