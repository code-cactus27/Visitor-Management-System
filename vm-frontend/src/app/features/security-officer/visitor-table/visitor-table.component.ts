import { Component, Input, OnChanges, OnInit, OnDestroy } from '@angular/core';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
import { Subscription, timer } from 'rxjs';
import { NotificationService } from 'src/app/core/services/notification.service';
@Component({
  selector: 'app-visitor-table',
  templateUrl: './visitor-table.component.html',
  styleUrls: ['./visitor-table.component.css']
})
export class VisitorTableComponent implements OnChanges, OnInit, OnDestroy {
  @Input() visits: any[] = [];
  paginatedVisits: any[] = [];
  allVisits: any[] = []; // Raw data buffer
  searchText: string = '';
  currentPage: number = 1;
  totalPages: number = 0;
  itemsPerPage: number = 6;
  private refreshSub?: Subscription;
  refreshIntervalMs = 500;
  constructor(
    public themeService: ThemeService,
    private visitorService: VisitorService,
    private notificationService: NotificationService
  ) {}
  ngOnInit(): void {
    // If used standalone (no parent passing visits), self-fetch from backend
    if (this.visits.length === 0) {
      this.loadVisits();
    }
    // Poll every 10s to keep the table fresh
    this.refreshSub = timer(10000, 10000).subscribe(() => {
      this.loadVisits();
    });
  }
  loadVisits(): void {
    this.visitorService.getVisitRecords().subscribe({
      next: (data) => {
        this.allVisits = data || [];
        this.onSearch(); // Apply current search to new data
      },
      error: (err) => console.error('Error fetching visit records:', err)
    });
  }
  onSearch(): void {
    if (!this.searchText) {
      this.visits = [...this.allVisits];
    } else {
      const term = this.searchText.toLowerCase();
      this.visits = this.allVisits.filter(v =>
        v.visitorName?.toLowerCase().includes(term) ||
        v.uniqueId?.toLowerCase().includes(term) ||
        v.company?.toLowerCase().includes(term)
      );
    }
    this.currentPage = 1;
    this.updatePagination();
  }
  ngOnChanges(): void {
    this.currentPage = 1;
    this.updatePagination();
  }
  ngOnDestroy(): void {
    this.refreshSub?.unsubscribe();
  }
  updatePagination(): void {
    this.totalPages = Math.max(1, Math.ceil(this.visits.length / this.itemsPerPage));
    if (this.currentPage > this.totalPages) {
      this.currentPage = this.totalPages;
    }
    if (this.currentPage < 1) {
      this.currentPage = 1;
    }
    this.updatePaginatedVisits();
  }
  updatePaginatedVisits(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedVisits = this.visits.slice(startIndex, endIndex);
  }
  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }
  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }
  formatTime(time: Date): string {
    return new Date(time).toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    });
  }
  approveVisitor(visitor: any) {
    this.visitorService.approveVisitor(visitor.visitId).subscribe(() => {
      visitor.statusOnTime = 'CHECKED_IN';
      const now = new Date();
      visitor.entryTime = now;
      this.notificationService.success(
        'Check-In Successful',
        `${visitor.visitorName} has been checked in at ${this.formatTime(now)}.`
      );
    });
  }
  rejectVisitor(id: number) {
    this.visitorService.rejectvisitor(id).subscribe(() => {
      const visitor = this.visits.find(v => v.visitId === id);
      if (visitor) {
        visitor.statusOnTime = 'EXPIRED';
        visitor.entryTime = null;
        visitor.exitTime = null;
        visitor.passExpiry = null;
        this.notificationService.warning('Visitor Rejected', `Access denied for ${visitor.visitorName}.`);
      }
      this.updatePagination();
    });
  }
  checkOutVisitor(id: number) {
    this.visitorService.checkOutVisitor(id).subscribe(() => {
      const visitor = this.visits.find(v => v.visitId === id);
      if (visitor) {
        visitor.statusOnTime = 'CHECKED_OUT';
        const now = new Date();
        visitor.exitTime = now;
        this.notificationService.info('Check-Out Successful', `${visitor.visitorName} has checked out.`);
      }
      this.updatePagination();
    });
  }
}
