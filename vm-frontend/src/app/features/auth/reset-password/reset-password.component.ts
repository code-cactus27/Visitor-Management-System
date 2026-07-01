import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  resetPasswordForm: FormGroup;
  loading = false;
  message = '';
  email = '';
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {
    this.resetPasswordForm = this.fb.group({
      newPassword: [
        '',
        [
          Validators.required,
          Validators.minLength(6)
        ]
      ]
    });
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.email = params['email'];
    });
  }
  get f() {
    return this.resetPasswordForm.controls;
  }
  resetPassword(): void {
    if (this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }
    const payload = {
      email: this.email,
      newPassword:
      this.resetPasswordForm.value.newPassword
    };
    this.loading = true;
    this.authService.resetPassword(payload)
      .subscribe({
        next: (response) => {
          this.loading = false;
          alert('Password Reset Successful');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.loading = false;
          this.message =
            error?.error || 'Reset Failed';
        }
      });
  }
}
