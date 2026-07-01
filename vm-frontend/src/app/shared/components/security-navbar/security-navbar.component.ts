import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';

import { AuthService }         from 'src/app/core/services/auth.service';
import { ThemeService }        from 'src/app/core/services/theme.service';
import { NotificationService } from 'src/app/core/services/notification.service';
import { EmergencyService, EmergencyAlert, EMERGENCY_TYPES } from 'src/app/core/services/emergency.service';

@Component({
  selector: 'app-security-navbar',
  templateUrl: './security-navbar.component.html',
  styleUrls:   ['./security-navbar.component.css']
})
export class SecurityNavbarComponent implements OnInit, OnDestroy {

  @Output() viewChange = new EventEmitter<string>();

  activeView    = '';
  isSidebarOpen = true;

  // ── Emergency ──────────────────────────────────────────────
  showEmergencyPicker = false;
  currentAlert: EmergencyAlert | null = null;
  customReason = '';            // bound to the custom-reason textarea

  // All types except 'custom' — shown as quick-pick buttons
  readonly predefinedTypes = EMERGENCY_TYPES.filter(t => t.type !== 'custom');

  private emergencySub?: Subscription;

  constructor(
    private authService:         AuthService,
    private router:              Router,
    public  themeService:        ThemeService,
    private notificationService: NotificationService,
    public  emergencyService:    EmergencyService
  ) {}

  ngOnInit(): void {
    this.emergencySub = this.emergencyService.alert$.subscribe(alert => {
      this.currentAlert = alert.active ? alert : null;
    });
  }

  ngOnDestroy(): void {
    this.emergencySub?.unsubscribe();
  }

  // ── Navigation ─────────────────────────────────────────────
  navigateTo(view: string): void {
    this.activeView = view;
    this.viewChange.emit(view);
    this.isSidebarOpen = false;
  }

  toggleSidebar():       void { this.isSidebarOpen = !this.isSidebarOpen; }
  toggleNotifications(): void { this.notificationService.toggleDrawer(); }

  // ── Emergency picker ───────────────────────────────────────
  openEmergencyPicker(): void {
    this.customReason = '';
    this.showEmergencyPicker = true;
  }

  closeEmergencyPicker(): void { this.showEmergencyPicker = false; }

  /** Raise a predefined type */
  raiseAlert(type: EmergencyAlert['type']): void {
    this.emergencyService.raise(type);
    this.showEmergencyPicker = false;
    this.customReason = '';
  }

  /** Raise the custom free-text alert */
  raiseCustomAlert(): void {
    if (!this.customReason.trim()) return;
    this.emergencyService.raiseCustom(this.customReason);
    this.showEmergencyPicker = false;
    this.customReason = '';
  }

  /** Dismiss — only callable by Security Officer */
  dismissAlert(): void {
    this.emergencyService.dismiss();
  }

  // ── Auth ───────────────────────────────────────────────────
  onLogout(): void {
    this.authService.logout().subscribe(
      () => this.router.navigate(['/login']),
      () => this.router.navigate(['/login'])
    );
  }
}


