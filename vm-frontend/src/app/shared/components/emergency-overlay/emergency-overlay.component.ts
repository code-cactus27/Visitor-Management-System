









import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { EmergencyAlert, EmergencyService } from 'src/app/core/services/emergency.service';
import { AuthService } from 'src/app/core/services/auth.service';

@Component({
  selector: 'app-emergency-overlay',
  templateUrl: './emergency-overlay.component.html',
  styleUrls:   ['./emergency-overlay.component.css']
})
export class EmergencyOverlayComponent implements OnInit, OnDestroy {

  alert: EmergencyAlert | null = null;
  private sub?: Subscription;

  constructor(
    private emergencyService: EmergencyService,
    private authService:      AuthService
  ) {}

  /**
   * True when the currently logged-in user is a Security Officer.
   * Only they see the Dismiss button on the overlay.
   * Computed as a getter so it reflects AuthService state at render time.
   */
  get isSecurity(): boolean {
    return this.authService.getRole() === 'SECURITY';
  }

  ngOnInit(): void {
    this.sub = this.emergencyService.alert$.subscribe(a => {
      this.alert = a.active ? a : null;
    });
  }

  ngOnDestroy(): void { this.sub?.unsubscribe(); }

  dismiss(): void {
    this.emergencyService.dismiss();
  }
}
