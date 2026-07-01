import { Component, Input, Output, EventEmitter, OnInit, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
@Component({
  selector: 'app-visitor-edit-form',
  templateUrl: './visitor-edit-form.component.html',
  styleUrls: ['./visitor-edit-form.component.css']
})
export class VisitorEditFormComponent implements OnInit, OnChanges {
  @Input() visitor: any;
// Keep close for explicit close actions
  @Output() close = new EventEmitter<void>();
// Rename meaning: parent should refresh table, not close modal
  @Output() saved = new EventEmitter<void>();
  visitorForm: FormGroup;
  loading: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  uniqueId: string = '';
  constructor(
    private fb: FormBuilder,
    public visitorService: VisitorService,
    public themeService: ThemeService
  ) {
    this.visitorForm = this.fb.group({
      name: ['', Validators.required],
      uniqueId: [{ value: '', disabled: true }],
      company: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern(/^[6-9][0-9]{9}$/)]],
      email: ['', [Validators.required, Validators.email]],
      notes: ['']
    });
  }
  ngOnInit(): void {
    this.patchFormFromInput();
  }
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['visitor']) {
      this.patchFormFromInput();
// reset messages when switching visitor
      this.successMessage = '';
      this.errorMessage = '';
    }
  }
  private patchFormFromInput(): void {
    if (!this.visitor) return;
    this.uniqueId = this.visitor.uniqueId || '';
    this.visitorForm.patchValue({
      name: this.visitor.name || '',
      uniqueId: this.visitor.uniqueId || '',
      company: this.visitor.company || '',
      contactNumber: this.visitor.contactNumber || '',
      email: this.visitor.email || '',
      notes: this.visitor.notes || ''
    });
  }
  submitForm(): void {
    this.errorMessage = '';
    this.successMessage = '';
    if (this.visitorForm.invalid) {
      this.visitorForm.markAllAsTouched();
      return;
    }
    if (!this.uniqueId) {
      this.errorMessage = 'Unique ID missing.';
      return;
    }
    this.loading = true;
    const payload = {
      name: this.visitorForm.get('name')?.value?.trim(),
      company: this.visitorForm.get('company')?.value?.trim(),
      contactNumber: this.visitorForm.get('contactNumber')?.value?.trim(),
      email: this.visitorForm.get('email')?.value?.trim(),
      notes: this.visitorForm.get('notes')?.value?.trim()
    };
    this.visitorService.updateVisitor(this.uniqueId, payload).subscribe({
      next: () => {
        this.loading = false;
// show message and keep modal open
        this.successMessage = 'Visitor updated successfully.';
        this.errorMessage = '';
// tell parent to refresh list ONLY
        this.saved.emit();
// Optional: if you want auto-close after 1.5s, uncomment:
// setTimeout(() => this.close.emit(), 1500);
      },
      error: (err) => {
        this.loading = false;
        this.successMessage = '';
        this.errorMessage = err?.error?.message || 'Failed to update visitor.';
      }
    });
  }
  handleEditClosed(): void {
    this.close.emit();
  }
}
