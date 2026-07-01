import {Component} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from 'src/app/core/services/auth.service';
@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotPasswordForm: FormGroup;
  loading = false;
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.forgotPasswordForm = this.fb.group({
      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      last4Digits: [
        '',
        [
          Validators.required,
          Validators.pattern('^[0-9]{4}$')
        ]
      ]
    });
  }
  get f() {
    return this.forgotPasswordForm.controls;
  }
  verifyUser(): void {
    console.log(this.forgotPasswordForm.value);
    if (this.forgotPasswordForm.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    this.authService.verifyUser(
      this.forgotPasswordForm.value
    ).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(
          ['/reset-password'],
          {
            queryParams: {
              email: this.forgotPasswordForm.value.email
            }
          }
        );
      },
      error: (error) => {
        this.loading = false;
        this.errorMessage = error.error.errorMessage;
      }
    });
  }
}
