// import { Component, OnInit } from '@angular/core';
// import { ActivatedRoute, RouterModule } from '@angular/router';
// import { CommonModule } from '@angular/common';
// import { HttpClient } from '@angular/common/http';
// import { ThemeService } from '../../../core/services/theme.service';
// /** Matches GatePassResponseDTO returned by GET /gate/gatepass/{visitorId}/visit/{visitId} */
// export interface GatePassData {
//   // Visitor
//   visitorId:      number;
//   uniqueId:       string;
//   name:           string;
//   company:        string;
//   contactNumber:  string;
//   email:          string;
//   notes:          string;
//   createdAt:      string;   // ISO LocalDateTime from backend
//   // Visit
//   visitId:        number;
//   reasonForVisit: string;
//   visitDate:      string;   // ISO LocalDate  e.g. "2026-05-16"
//   expectedTime:   string;   // ISO LocalTime  e.g. "09:30:00"
//   passDuration:   number;
//   entryTime:      string | null;
//   exitTime:       string | null;
//   passExpiry:     string | null;
//   /** PENDING | CHECKED_IN | CHECKED_OUT | EXPIRED */
//   status: string;
// }
// @Component({
//   selector: 'app-gatepass',
//   standalone: true,
//   imports: [CommonModule, RouterModule],
//   templateUrl: './gatepass.component.html',
//   styleUrls: ['./gatepass.component.css']
// })
// export class GatepassComponent implements OnInit {
//   gatePassData: GatePassData | null = null;
//   loading  = true;
//   error: string | null = null;
//   // Derived display metadata (set by applyStatusMeta)
//   statusLabel = '';   // text shown in the stamp badge
//   statusClass = '';   // CSS class for colour  (checked_in | checked_out | expired | pending)
//   statusImage = '';   // path to stamp PNG in /assets/
//   iconClass   = '';   // Font Awesome icon class
//   private readonly BASE_URL = 'http://localhost:8080/gate';
//   constructor(
//     private route:        ActivatedRoute,
//     private http:         HttpClient,
//     public  themeService: ThemeService
//   ) {}
//   ngOnInit(): void {
//     const visitorId = Number(this.route.snapshot.paramMap.get('visitorId'));
//     const visitId   = Number(this.route.snapshot.paramMap.get('visitId'));
//     if (!visitorId || !visitId) {
//       this.error   = 'Invalid visitor or visit ID in the URL.';
//       this.loading = false;
//       return;
//     }
//     this.fetchGatePass(visitorId, visitId);
//   }
//   private fetchGatePass(visitorId: number, visitId: number): void {
//     this.http
//       .get<GatePassData>(`${this.BASE_URL}/gatepass/${visitorId}/visit/${visitId}`)
//       .subscribe({
//         next: (data) => {
//           this.gatePassData = data;
//           this.applyStatusMeta(data.status);
//           this.loading = false;
//         },
//         error: (err) => {
//           this.error   = err?.error?.message ?? 'Failed to load gate pass. Please try again.';
//           this.loading = false;
//         }
//       });
//   }
//   /**
//    * Maps the 4 possible statuses to display metadata:
//    *
//    *  CHECKED_IN  → green approved stamp  + check-circle icon
//    *  EXPIRED     → red   rejected stamp  + times-circle icon   (rejected)
//    *  CHECKED_OUT → red   rejected stamp  + sign-out-alt icon
//    *  PENDING     → no stamp              + clock icon
//    */
//   private applyStatusMeta(status: string): void {
//     switch (status) {
//       case 'CHECKED_IN':
//         this.statusLabel = 'VERIFIED & APPROVED';
//         this.statusClass = 'checked_in';
//         this.statusImage = 'assets/approved.png';
//         this.iconClass   = 'fa-check-circle';
//         break;
//       case 'EXPIRED':
//         this.statusLabel = 'REJECTED';
//         this.statusClass = 'expired';
//         this.statusImage = 'assets/rejected.png';
//         this.iconClass   = 'fa-times-circle';
//         break;
//       case 'CHECKED_OUT':
//         this.statusLabel = 'CHECKED OUT';
//         this.statusClass = 'checked_out';
//         this.statusImage = 'assets/rejected.png';   // red stamp for checkout
//         this.iconClass   = 'fa-sign-out-alt';
//         break;
//       case 'PENDING':
//       default:
//         this.statusLabel = 'PENDING APPROVAL';
//         this.statusClass = 'pending';
//         this.statusImage = '';   // no stamp image while still pending
//         this.iconClass   = 'fa-clock';
//         break;
//     }
//   }
//   printPass(): void {
//     window.print();
//   }
// }
import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, RouterLink} from '@angular/router';

/** Matches GatePassResponseDTO returned by
 *  GET /gate/gatepass/{visitorId}/visit/{visitId}
 */
export interface GatePassData {
  // ── Visitor ──────────────────────────────────
  visitorId:     number;
  uniqueId:      string;
  name:          string;
  company:       string;
  contactNumber: string;
  email:         string;
  notes:         string;
  createdAt:     string;        // "2026-05-16T09:15:00"

  // ── Visit ─────────────────────────────────────
  visitId:        number;
  reasonForVisit: string;
  visitDate:      string;       // "2026-05-16"
  expectedTime:   string;       // "09:30:00"
  passDuration:   number;
  entryTime:      string | null; // "2026-05-16T09:35:00"
  exitTime:       string | null;
  passExpiry:     string | null;

  /** PENDING | CHECKED_IN | CHECKED_OUT | EXPIRED */
  status: string;
}

import { ThemeService } from '../../../core/services/theme.service';
import { HttpClient } from '@angular/common/http';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-gatepass',
  templateUrl: './gatepass.component.html',
  standalone: true,
  imports: [CommonModule, RouterLink],
  styleUrls: ['./gatepass.component.css']
  // NOT standalone — declared in AppModule / security feature module
})
export class GatepassComponent implements OnInit {

  gatePassData: GatePassData | null = null;
  loading = true;
  error: string | null = null;

  // ── Status-derived display props ──────────────
  statusLabel = '';
  statusClass = '';   // checked_in | checked_out | expired | pending
  statusImage = '';   // path in /assets/
  iconClass   = '';   // Font Awesome class

  constructor(
    private route:        ActivatedRoute,
    private http:         HttpClient,
    public  themeService: ThemeService
  ) {}

  ngOnInit(): void {
    // Route: /security/gatepass/:visitorId/visit/:visitId
    const visitorId = Number(this.route.snapshot.paramMap.get('visitorId'));
    const visitId   = Number(this.route.snapshot.paramMap.get('visitId'));

    if (!visitorId || !visitId) {
      this.error   = 'Invalid visitor or visit ID in the URL.';
      this.loading = false;
      return;
    }

    this.fetchGatePass(visitorId, visitId);
  }

  private fetchGatePass(visitorId: number, visitId: number): void {
    // Use the proxy path — no hardcoded localhost
    this.http
      .get<GatePassData>(`/gate/gatepass/${visitorId}/visit/${visitId}`)
      .subscribe({
        next: (data) => {
          this.gatePassData = data;
          this.applyStatusMeta(data.status);
          this.loading = false;
        },
        error: (err) => {
          this.error   = err?.error?.message ?? 'Failed to load gate pass. Please try again.';
          this.loading = false;
        }
      });
  }

  /**
   * 4 status variants:
   *
   *  CHECKED_IN  → blue header  · approved.png  · fa-check-circle
   *  EXPIRED     → red header   · rejected.png  · fa-times-circle   (rejected)
   *  CHECKED_OUT → slate header · rejected.png  · fa-sign-out-alt
   *  PENDING     → amber header · no image      · fa-clock
   */
  private applyStatusMeta(status: string): void {
    switch (status) {
      case 'CHECKED_IN':
        this.statusLabel = 'VERIFIED & APPROVED';
        this.statusClass = 'checked_in';
        this.statusImage = 'assets/approved.png';
        this.iconClass   = 'fa-check-circle';
        break;

      case 'EXPIRED':
        this.statusLabel = 'REJECTED';
        this.statusClass = 'expired';
        this.statusImage = 'assets/rejected.png';
        this.iconClass   = 'fa-times-circle';
        break;

      case 'CHECKED_OUT':
        this.statusLabel = 'CHECKED OUT';
        this.statusClass = 'checked_out';
        this.statusImage = 'assets/rejected.png';
        this.iconClass   = 'fa-sign-out-alt';
        break;

      case 'PENDING':
      default:
        this.statusLabel = 'PENDING APPROVAL';
        this.statusClass = 'pending';
        this.statusImage = '';
        this.iconClass   = 'fa-clock';
        break;
    }
  }

  /**
   * Convert a plain "HH:mm:ss" string from the backend into a
   * full ISO string so Angular's date pipe can format it.
   * Returns null if the input is null / undefined / empty.
   */
  toTimeString(t: string | null | undefined): string | null {
    if (!t) return null;
    // If it already looks like an ISO datetime, return as-is
    if (t.includes('T')) return t;
    // Prefix a dummy date so the pipe receives a valid ISO string
    return `1970-01-01T${t}`;
  }

  printPass(): void {
    window.print();
  }
}
