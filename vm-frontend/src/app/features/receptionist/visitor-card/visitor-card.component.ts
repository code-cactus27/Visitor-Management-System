import {
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnDestroy
} from '@angular/core';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
@Component({
  selector: 'app-visitor-card',
  templateUrl: './visitor-card.component.html',
  styleUrls: ['./visitor-card.component.css']
})
export class VisitorCardComponent implements OnInit, OnDestroy {
  /** Full visitor object from VisitorResponseDTO */
  @Input() visitor: any;
  @Output() editVisitor   = new EventEmitter<any>();
  @Output() addVisit      = new EventEmitter<any>();
  @Output() deleteVisitor = new EventEmitter<any>();
  // ─── Visit history state ─────────────────────────────────────
  showHistory    = false;
  historyLoading = false;
  historyError   = '';
  visits: any[]  = [];
  // ─── Visit summary counts (computed after load) ──────────────
  checkedInCount = 0;
  pendingCount   = 0;
  totalCount     = 0;
  hasCheckedIn   = false;
  hasPending     = false;
  // ─── Delete confirmation ──────────────────────────────────────
  showDeleteConfirm = false;
  // ─── Card expand / collapse ───────────────────────────────────
  isExpanded = false;
  constructor(
    public  themeService:   ThemeService,
    private visitorService: VisitorService
  ) {}
  ngOnInit():    void {}
  ngOnDestroy(): void {}
  // ─── Avatar initials ─────────────────────────────────────────
  get initials(): string {
    const parts = (this.visitor?.name || '').trim().split(' ');
    if (parts.length >= 2) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return (parts[0]?.[0] || '?').toUpperCase();
  }
  // ─── Avatar background colour (deterministic from name) ──────
  get avatarColor(): string {
    const colors = [
      '#2563eb', '#7c3aed', '#0891b2', '#059669',
      '#d97706', '#dc2626', '#db2777', '#4f46e5'
    ];
    let hash = 0;
    for (const ch of (this.visitor?.name || '')) {
      hash = ch.charCodeAt(0) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash) % colors.length];
  }
  // ─── Expand toggle ────────────────────────────────────────────
  toggleExpand(): void { this.isExpanded = !this.isExpanded; }
  // ─── Load visit history ───────────────────────────────────────
  loadHistory(): void {
    this.showHistory    = true;
    this.historyLoading = true;
    this.historyError   = '';
    this.visits         = [];
    this.visitorService.getVisitsByVisitor(this.visitor.uniqueId).subscribe({
      next: (data) => {
        this.visits         = data || [];
        this.historyLoading = false;
        this.computeSummary();
      },
      error: (err) => {
        this.historyError   = err?.error?.message || 'Failed to load visit history.';
        this.historyLoading = false;
      }
    });
  }
  /** Compute summary counts without any pipes */
  private computeSummary(): void {
    this.totalCount     = this.visits.length;
    this.checkedInCount = this.visits.filter(v => v.statusOnTime === 'CHECKED_IN').length;
    this.pendingCount   = this.visits.filter(v => v.statusOnTime === 'PENDING').length;
    this.hasCheckedIn   = this.checkedInCount > 0;
    this.hasPending     = this.pendingCount   > 0;
  }
  closeHistory(): void {
    this.showHistory  = false;
    this.visits       = [];
    this.historyError = '';
    this.checkedInCount = this.pendingCount = this.totalCount = 0;
    this.hasCheckedIn   = this.hasPending   = false;
  }
  // ─── Status CSS class ─────────────────────────────────────────
  statusClass(status: string): string {
    switch (status) {
      case 'CHECKED_IN':  return 'checkedIn';
      case 'CHECKED_OUT': return 'checkedOut';
      case 'EXPIRED':     return 'expired';
      default:            return 'pending';
    }
  }
  // ─── Delete flow ─────────────────────────────────────────────
  openDeleteConfirm(): void  { this.showDeleteConfirm = true;  }
  cancelDelete():      void  { this.showDeleteConfirm = false; }
  confirmDelete():     void  {
    this.deleteVisitor.emit(this.visitor);
    this.showDeleteConfirm = false;
  }
}
