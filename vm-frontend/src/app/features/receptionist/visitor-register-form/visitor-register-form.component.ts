import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
import { NotificationService } from 'src/app/core/services/notification.service';
@Component({
  selector: 'app-visitor-register-form',
  templateUrl: './visitor-register-form.component.html',
  styleUrls: ['./visitor-register-form.component.css']
})
export class VisitorRegisterFormComponent implements OnInit {
  visitorForm: FormGroup;
  visitors: any[] = [];
  showForm = false;
  isEditOpen = false;
  showVisit = false;
  selectedVisitor: any = null;
  isDark = true;
  @Output() close = new EventEmitter<void>();
  @Output() saved = new EventEmitter<void>(); // parent should refresh on this
  loading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  isExistingVisitMode = false;
  selectedUniqueId: string = '';
  // ✅ More duration options (hours)
  durationOptions: number[] = [1, 2, 3, 4, 6, 8, 10, 12, 24];
  reasonOptions: string[] = [
    'Meeting',
    'Interview',
    'Delivery / Courier',
    'Maintenance',
    'Vendor Visit',
    'Training / Workshop',
    'Client Support'
  ];
  // ✅ After success: hide fields and show only success message
  showSuccessOnly: boolean = false;
  constructor(
    private fb: FormBuilder,
    private visitorService: VisitorService,
    public themeService: ThemeService,
    private notificationService: NotificationService
  ) {
    this.visitorForm = this.fb.group({
      name: ['', [Validators.required, Validators.pattern('^[A-Z][a-z]{2,}( [A-Z][a-z]+){0,2}$')]],
      company: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern('^[6-9][0-9]{9}$')]],
      email: ['', [Validators.required, Validators.email]],
      notes: [''],
      reasonForVisit: ['', Validators.required],
      passDuration: [null, Validators.required],
      visitDate: ['', [Validators.required, this.futureDateValidator.bind(this)]],
      expectedTime: ['', [Validators.required, this.futureTimeValidator.bind(this)]]
    });
  }
  ngOnInit() {
    this.loadVisitors();
    // ✅ When date changes, re-check time validity
    this.visitorForm.get('visitDate')?.valueChanges.subscribe(() => {
      this.visitorForm.get('expectedTime')?.updateValueAndValidity();
    });
  }
  private resetUiState(): void {
    this.loading = false;
    this.successMessage = '';
    this.errorMessage = '';
    this.showSuccessOnly = false;
  }
  loadVisitors() {
    this.visitorService.getVisitors().subscribe({
      next: (data) => {
        this.visitors = data;
      },
      error: (err) => {
        console.error('Failed to load visitors', err);
      }
    });
  }
  toggleTheme() {
    this.isDark = !this.isDark;
  }
  openForm() {
    this.resetUiState();
    this.isExistingVisitMode = false;
    this.selectedUniqueId = '';
    this.showForm = true;
    this.visitorForm.reset();
    this.enableVisitorFields();
  }
  closeForm() {
    this.showForm = false;
    this.isExistingVisitMode = false;
    this.selectedUniqueId = '';
    this.enableVisitorFields();
    this.resetUiState();
    this.close.emit();
    this.ngOnInit();
  }
  enableVisitorFields() {
    this.visitorForm.get('name')?.enable();
    this.visitorForm.get('company')?.enable();
    this.visitorForm.get('contactNumber')?.enable();
    this.visitorForm.get('email')?.enable();
    this.visitorForm.get('notes')?.enable();
  }
  openAddVisit(v: any) {
    this.resetUiState();
    this.isExistingVisitMode = true;
    this.showForm = true;
    this.selectedUniqueId = v.uniqueId;
    this.visitorForm.patchValue({
      name: v.name || '',
      company: v.company || '',
      contactNumber: v.contactNumber || '',
      email: v.email || '',
      notes: v.notes || '',
      reasonForVisit: '',
      passDuration: '',
      visitDate: '',
      expectedTime: ''
    });
    // lock visitor fields in existing-visit mode
    this.visitorForm.get('name')?.disable();
    this.visitorForm.get('company')?.disable();
    this.visitorForm.get('contactNumber')?.disable();
    this.visitorForm.get('email')?.disable();
    this.visitorForm.get('notes')?.disable();
  }
  submitForm() {
    this.successMessage = '';
    this.errorMessage = '';
    if (this.visitorForm.invalid) {
      this.visitorForm.markAllAsTouched();
      return;
    }
    this.loading = true;
    const f = this.visitorForm.getRawValue();
    if (this.isExistingVisitMode) {
      const existingPayload = {
        uniqueId: this.selectedUniqueId,
        reasonForVisit: f.reasonForVisit,
        passDuration: Number(f.passDuration),
        visitDate: f.visitDate,
        expectedTime: f.expectedTime
      };
      this.visitorService.addVisitExisting(existingPayload).subscribe({
        next: (res: string) => {
          this.onSubmitSuccess(res || 'Visit added successfully.');
        },
        error: (err) => {
          // backend sometimes returns text under error even with 200/201
          if ((err.status === 200 || err.status === 201) && err.error?.text) {
            this.onSubmitSuccess(err.error.text);
            return;
          }
          this.loading = false;
          this.successMessage = '';
          this.errorMessage = err?.error?.message || 'Failed to add visit.';
        }
      });
    } else {
      const newPayload = {
        name: f.name?.trim(),
        company: f.company?.trim(),
        contactNumber: f.contactNumber?.trim(),
        email: f.email?.trim(),
        notes: f.notes?.trim(),
        reasonForVisit: f.reasonForVisit,
        passDuration: Number(f.passDuration),
        visitDate: f.visitDate,
        expectedTime: f.expectedTime
      };
      this.visitorService.addVisitNew(newPayload).subscribe({
        next: (res: any) => {
          let msg = 'Registration successful.';
          if (typeof res === 'string') {
            msg = `Registration successful. ${res}`;
            // Try to extract ID from string response if possible, or leave as New
          } else if (res && res.uniqueId) {
            this.selectedUniqueId = res.uniqueId;
          }
          this.onSubmitSuccess(msg);
        },
        error: (err) => {
          if ((err.status === 200 || err.status === 201) && err.error?.text) {
            this.onSubmitSuccess(err.error.text);
            return;
          }
          this.loading = false;
          this.successMessage = '';
          this.errorMessage = err?.error?.message || 'Failed to add visit.';
        }
      });
    }
  }
  /**
   * ✅ Centralized success handler:
   * - show only message (hide fields)
   * - refresh visitors automatically
   * - notify parent for refreshing other components
   */
  private onSubmitSuccess(message: string): void {
    this.loading = false;
    this.errorMessage = '';
    this.successMessage = message;
    this.showSuccessOnly = true;
    // Notify parent
    this.saved.emit();
    this.loadVisitors();
    // Trigger colorful notification
    const f = this.visitorForm.getRawValue();
    const visitorName = f.name || 'Visitor';
    const id = this.selectedUniqueId || 'New';
    this.notificationService.show(
      'Check-in Successful',
      `Welcome ${visitorName}! ID: ${id}`,
      'success'
    );
  }
  // Date cannot be in the past (today allowed)
  futureDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    if (!control.value) return null;
    const selected = this.toDateOnly(control.value);
    const today = this.toDateOnly(new Date());
    return selected < today ? { pastDate: true } : null;
  }
  // If visitDate is future => any time ok; if today => must be > now
  futureTimeValidator(control: AbstractControl): { [key: string]: boolean } | null {
    if (!control.value) return null;
    const dateValue = this.visitorForm?.get('visitDate')?.value;
    if (!dateValue) return null;
    const selectedDateOnly = this.toDateOnly(dateValue);
    const todayDateOnly = this.toDateOnly(new Date());
    // Future date => time doesn't matter
    if (selectedDateOnly.getTime() > todayDateOnly.getTime()) return null;
    // Today => must be in future
    if (selectedDateOnly.getTime() === todayDateOnly.getTime()) {
      const [hh, mm] = String(control.value).split(':').map((x) => Number(x));
      if (Number.isNaN(hh) || Number.isNaN(mm)) return { invalidTime: true };
      const now = new Date();
      const selectedDateTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hh, mm, 0, 0);
      if (selectedDateTime <= now) return { pastTime: true };
    }
    return null;
  }
  private toDateOnly(input: string | Date): Date {
    const d = input instanceof Date ? input : new Date(input);
    return new Date(d.getFullYear(), d.getMonth(), d.getDate());
  }
}
