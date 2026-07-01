import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfigService } from 'src/app/core/services/config.service';
import { ThemeService } from 'src/app/core/services/theme.service';
@Component({
  selector: 'app-config',
  templateUrl: './config.component.html',
  styleUrls: ['./config.component.css']
})
export class ConfigComponent {
  form!: FormGroup;
  successMsg = '';
  constructor(
    private fb: FormBuilder,
    private configService: ConfigService,
    public themeService:ThemeService
  ) {}
  ngOnInit(): void {
    this.initForm();
    this.loadConfig();
  }
  initForm() {
    this.form = this.fb.group({
      GATE_PASS_DEFAULT_DURATION: ['', [Validators.required]],
      GATE_PASS_MAX_DURATION: ['', [Validators.required]],
      EMAIL_SENDER: ['', [Validators.required, Validators.email]],
      EMAIL_RECEIVER: ['', [Validators.required, Validators.email]]
    });
  }
  loadConfig() {
    this.configService.getConfig().subscribe({
      next: (data) => {
        this.form.patchValue(data);
      },
      error: () => {
      }
    });
  }
  submit() {
    if (this.form.invalid) return;
    const val = this.form.value;
    if (+val.GATE_PASS_DEFAULT_DURATION > +val.GATE_PASS_MAX_DURATION) {
      alert('Default duration cannot be greater than max duration');
      return;
    }
    this.configService.updateConfig(val).subscribe(() => {
      this.successMsg = 'Configuration updated successfully';
      setTimeout(() => this.successMsg = '', 3000);
    });
  }
}
