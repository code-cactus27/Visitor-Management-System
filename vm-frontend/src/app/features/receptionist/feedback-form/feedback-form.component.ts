import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from 'src/app/core/services/theme.service';
import { FeedbackService } from 'src/app/core/services/feedback.service';
@Component({
  selector: 'app-feedback-form',
  templateUrl: './feedback-form.component.html',
  styleUrls: ['./feedback-form.component.css']
})
export class FeedbackFormComponent {
  feedbackForm: FormGroup;
  rating = 0;
  ratingError = false;
  loading = false;
  successMessage = '';
  errorMessage = '';
  constructor(
    private fb: FormBuilder,
    public themeService: ThemeService,
    private feedbackService: FeedbackService
  ) {
    this.feedbackForm = this.fb.group({
      visitorName: [
        '',
        [
          Validators.required,
          Validators.pattern('^[A-Za-z ]{3,50}$')
        ]
      ],
      visitorId: [
        '',
        [
          Validators.required,
          Validators.pattern('^[0-9]{6}[A-Z][0-9]{5}$')
        ]
      ],
      feedbackText: [
        '',
        [
          Validators.required,
          Validators.minLength(10)
        ]
      ]
    });
  }
  setRating(value: number) {
    this.rating = value;
    this.ratingError = false;
  }
  submitFeedback() {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.feedbackForm.invalid) {
      this.feedbackForm.markAllAsTouched();
      return;
    }
    if (this.rating === 0) {
      this.ratingError = true;
      return;
    }
    this.loading = true;
    const payload = {
      visitorId: this.feedbackForm.value.visitorId,
      visitorName: this.feedbackForm.value.visitorName,
      feedbackText: this.feedbackForm.value.feedbackText,
      rating: this.rating
    };
    console.log(payload);
    this.feedbackService.submitFeedback(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.successMessage =
          'Feedback Submitted Successfully';
        this.errorMessage = '';
        this.feedbackForm.reset();
        this.rating = 0;
      },
      error: (err) => {
        this.loading = false;
        this.successMessage = '';
        this.errorMessage =
          err?.error?.message ||
          'Failed to submit feedback';
      }
    });
  }
  @Output() close = new EventEmitter<void>();
  closeForm() {
    this.close.emit();
  }
}
