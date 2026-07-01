import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
@Component({
  selector: 'app-receptionist',
  templateUrl: './receptionist.component.html',
  styleUrls: ['./receptionist.component.css']
})
export class ReceptionistComponent {
  visitorForm: FormGroup;
  visitors: any[] = [];
  showForm = false;
  isEditOpen = false;
  showVisit = false;
  selectedVisitor: any = null;
  activeView: string = 'home';
  isDark = true; // toggle
  showFeedbackForm: boolean=false;
  constructor(
    private fb: FormBuilder,
    private visitorService: VisitorService, public themeService:ThemeService
  ) {
    this.visitorForm = this.fb.group({
      name:['',[Validators.required,Validators.pattern('^[A-Z][a-z]{2,}( [A-Z][a-z]+){0,2}$')]],
      company:['',Validators.required],
      contactNumber:['',[Validators.required,Validators.pattern('^[6-9][0-9]{9}$')]],
      email:['',[Validators.required,Validators.email]],
      notes:[''],
      reason:['',Validators.required],
      visitNotes:[''],
      duration:['',Validators.required]
    });
  }
  openForm() { this.showForm = true; }
  closeForm() { this.showForm = false; }
  onViewChange(view: string) { this.activeView = view; }
  openEdit(v: any) {
    this.selectedVisitor = v;
    this.isEditOpen = true;
  }
  closeEdit() {
    this.isEditOpen=false;
    console.log(this.isEditOpen);
  }
  submitForm() {
    if (this.visitorForm.invalid) {
      this.visitorForm.markAllAsTouched();
      return;
    }
    console.log(this.visitorForm.value);
  }
  openFeedbackForm() {
    this.showFeedbackForm = true;
  }
  closeFeedbackForm() {
    this.showFeedbackForm = false;
  }
}
