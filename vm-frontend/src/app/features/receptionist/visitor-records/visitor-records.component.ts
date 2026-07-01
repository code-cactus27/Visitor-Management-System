// import { Component, OnDestroy, ViewChild } from '@angular/core';
// import { Router } from '@angular/router';
// import { ThemeService } from 'src/app/core/services/theme.service';
// import { VisitorService } from 'src/app/core/services/visitor.service';
// import { VisitorRegisterFormComponent } from '../visitor-register-form/visitor-register-form.component';
// @Component({
//   selector: 'app-visitor-records',
//   templateUrl: './visitor-records.component.html',
//   styleUrls: ['./visitor-records.component.css']
// })
// export class VisitorRecordsComponent implements OnDestroy {
//   visitors: any[] = [];
//   isEditOpen = false;
//   selectedVisitor: any;
//   isExistingVisitMode = false;
//   searchText: string = '';
//   fromDate: string = '';
//   toDate: string = '';
//   currentPage = 1;
//   itemsPerPage = 5;
//   showVisit: boolean = false;
//   showVisitHistory: boolean = false;
//   visitHistoryLoading = false;
//   visitHistoryError = '';
//   visitHistoryTitle = '';
//   selectedVisitorVisits: any[] = [];
//   @ViewChild(VisitorRegisterFormComponent) visitorRegisterFormComponent?: VisitorRegisterFormComponent;
//   private refreshTimer?: number;
//   constructor(
//     public themeService: ThemeService,
//     private visitorService: VisitorService,
//     private router: Router
//   ) { }
//   ngOnInit() {
//     this.loadVisitors();
//     this.refreshTimer = window.setInterval(() => this.loadVisitors(), 2000);
//   }
//   ngOnDestroy() {
//     if (this.refreshTimer) {
//       clearInterval(this.refreshTimer);
//     }
//   }
//   loadVisitors() {
//     this.visitorService.getVisitors().subscribe({
//       next: (data) => {
//         this.visitors = data;
//       },
//       error: (err) => {
//         console.error('Failed to load visitors', err);
//       }
//     });
//   }
//   openEdit(v: any) {
//     this.selectedVisitor = v;
//     //  this.router.navigate(['/visitor-edit', v.uniqueId]);
//     this.isEditOpen = true;
//   }
//   closeEdit() {
//     this.isEditOpen = false;
//     this.selectedVisitor = null;
//   }
//   onSaved() {
//     this.loadVisitors();
//     // this.closeEdit();
//   }
//   // called from Actions button
//   openVisit(v: any) {
//     this.selectedVisitor = v;
//     this.showVisit = true;
//     // wait one tick so child exists, then call child's existing-visit mode
//     setTimeout(() => {
//       this.visitorRegisterFormComponent?.openAddVisit(v);
//     });
//   }
//   closeVisit() {
//     this.showVisit = false;
//     this.selectedVisitor = null;
//     this.loadVisitors(); // refresh list after add visit/new register
//   }
//   openViewVisits(v: any) {
//     this.showVisitHistory = true;
//     this.visitHistoryLoading = true;
//     this.visitHistoryError = '';
//     this.selectedVisitorVisits = [];
//     this.visitHistoryTitle = `${v.name} (${v.uniqueId})`;
//     console.log(v.uniqueId);
//     console.log('http:localhost:8765/gate/visit/visitor/' + v.uniqueId)
//     this.visitorService.getVisitsByVisitor(v.uniqueId).subscribe({
//       next: (data) => {
//         this.selectedVisitorVisits = data || [];
//         this.visitHistoryLoading = false;
//       },
//       error: (err) => {
//         this.visitHistoryLoading = false;
//         this.visitHistoryError = err?.error?.message || 'Failed to load visit history.';
//       }
//     });
//   }
//   deleteVisitor(v: any) {
//     const confirmDate = confirm(`Are you sure you want to delete ${v.name}`);
//     if (!confirmDate) return;
//     this.visitorService.deleteVisitor(v.uniqueId).subscribe({
//       next: (res) => {
//         console.log("delete clicked")
//         alert(res);
//         this.loadVisitors();
//       },
//       error: (err) => {
//         alert("Failed to delete Visitor");
//         console.error(err)
//         console.log("delete failed")
//       }
//     });
//   }
//   showDeletePopup = false;
//   visitorToDelete: any = null;
//   openDeletePopup(v: any) {
//     this.visitorToDelete = v;
//     this.showDeletePopup = true;
//   }
//   cancelDelete() {
//     this.showDeletePopup = false;
//     this.visitorToDelete = null;
//   }
//   confirmDelete() {
//     if (!this.visitorToDelete) return;
//     this.visitorService.deleteVisitor(this.visitorToDelete.uniqueId).subscribe({
//       next: (res) => {
//         this.loadVisitors();
//         this.cancelDelete();
//       },
//       error: () => {
//         this.cancelDelete();
//       }
//     });
//   }
//   closeViewVisits() {
//     this.showVisitHistory = false;
//     this.selectedVisitorVisits = [];
//     this.visitHistoryError = '';
//     this.visitHistoryTitle = '';
//   }
//   get filterVisitors() {
//     const search = this.searchText?.toLowerCase().trim() || '';
//     return this.visitors.filter(v => {
//       // SEARCH FILTER
//       const matchesSearch =
//         v.name?.toLowerCase().includes(search) ||
//         v.uniqueId?.toLowerCase().includes(search);
//       // DATE FILTER
//       let matchesDate = true;
//       if (this.fromDate || this.toDate) {
//         // visitor created date
//         const visitDate = new Date(v.createdAt);
//         visitDate.setHours(0, 0, 0, 0);
//         // FROM DATE
//         if (this.fromDate) {
//           const from = new Date(this.fromDate);
//           from.setHours(0, 0, 0, 0);
//           if (visitDate < from) {
//             matchesDate = false;
//           }
//         }
//         // TO DATE
//         if (this.toDate) {
//           const to = new Date(this.toDate);
//           to.setHours(23, 59, 59, 999);
//           if (visitDate > to) {
//             matchesDate = false;
//           }
//         }
//       }
//       return matchesSearch && matchesDate;
//     });
//   }
//   // pagination
//   get paginatedVisitors() {
//     const start = (this.currentPage - 1) * this.itemsPerPage;
//     return this.filterVisitors.slice(start, start + this.itemsPerPage);
//   }
//   get totalPages() {
//     return Math.ceil(this.filterVisitors.length / this.itemsPerPage) || 1;
//   }
//   nextPage() {
//     if (this.currentPage < this.totalPages) this.currentPage++;
//   }
//   prevPage() {
//     if (this.currentPage > 1) this.currentPage--;
//   }
// }
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
import { VisitorRegisterFormComponent } from '../visitor-register-form/visitor-register-form.component';
@Component({
  selector: 'app-visitor-records',
  templateUrl: './visitor-records.component.html',
  styleUrls: ['./visitor-records.component.css']
})
export class VisitorRecordsComponent implements OnInit, OnDestroy {
  visitors: any[] = [];
  // ─── View mode ───────────────────────────────────────────────
  viewMode: 'table' | 'card' = 'table';
  // ─── Edit / register ─────────────────────────────────────────
  isEditOpen          = false;
  selectedVisitor: any;
  isExistingVisitMode = false;
  showVisit           = false;
  // ─── Filters ─────────────────────────────────────────────────
  searchText = '';
  fromDate   = '';
  toDate     = '';
  // ─── Pagination ──────────────────────────────────────────────
  currentPage  = 1;
  itemsPerPage = 8;
  // ─── Visit history (table-view modal) ────────────────────────
  showVisitHistory       = false;
  visitHistoryLoading    = false;
  visitHistoryError      = '';
  visitHistoryTitle      = '';
  selectedVisitorVisits: any[] = [];
  // ─── Delete (table-view modal) ────────────────────────────────
  showDeletePopup  = false;
  visitorToDelete: any = null;
  @ViewChild(VisitorRegisterFormComponent)
  visitorRegisterFormComponent?: VisitorRegisterFormComponent;
  private refreshTimer?: number;
  constructor(
    public  themeService:   ThemeService,
    private visitorService: VisitorService,
    private router:         Router
  ) {}
  ngOnInit(): void {
    this.loadVisitors();
    this.refreshTimer = window.setInterval(() => this.loadVisitors(), 200000);
  }
  ngOnDestroy(): void {
    if (this.refreshTimer) clearInterval(this.refreshTimer);
  }
  // ─── Data ─────────────────────────────────────────────────────
  loadVisitors(): void {
    this.visitorService.getVisitors().subscribe({
      next:  (data) => { this.visitors = data; },
      error: (err)  => { console.error('Failed to load visitors', err); }
    });
  }
  // ─── Edit ─────────────────────────────────────────────────────
  openEdit(v: any): void {
    this.selectedVisitor = v;
    this.isEditOpen      = true;
  }
  closeEdit(): void {
    this.isEditOpen      = false;
    this.selectedVisitor = null;
  }
  onSaved(): void { this.loadVisitors(); }
  // ─── Add Visit ───────────────────────────────────────────────
  openVisit(v: any): void {
    this.selectedVisitor = v;
    this.showVisit       = true;
    setTimeout(() => {
      this.visitorRegisterFormComponent?.openAddVisit(v);
    });
  }
  closeVisit(): void {
    this.showVisit       = false;
    this.selectedVisitor = null;
    this.loadVisitors();
  }
  // ─── Visit History (table-view modal) ────────────────────────
  openViewVisits(v: any): void {
    this.showVisitHistory      = true;
    this.visitHistoryLoading   = true;
    this.visitHistoryError     = '';
    this.selectedVisitorVisits = [];
    this.visitHistoryTitle     = `${v.name} (${v.uniqueId})`;
    this.visitorService.getVisitsByVisitor(v.uniqueId).subscribe({
      next:  (data) => {
        this.selectedVisitorVisits = data || [];
        this.visitHistoryLoading   = false;
      },
      error: (err) => {
        this.visitHistoryLoading = false;
        this.visitHistoryError   = err?.error?.message || 'Failed to load visit history.';
      }
    });
  }
  closeViewVisits(): void {
    this.showVisitHistory      = false;
    this.selectedVisitorVisits = [];
    this.visitHistoryError     = '';
    this.visitHistoryTitle     = '';
  }
  // ─── Delete (table-view modal) ────────────────────────────────
  openDeletePopup(v: any): void {
    this.visitorToDelete = v;
    this.showDeletePopup = true;
  }
  cancelDelete(): void {
    this.showDeletePopup = false;
    this.visitorToDelete = null;
  }
  confirmDelete(): void {
    if (!this.visitorToDelete) return;
    this.visitorService.deleteVisitor(this.visitorToDelete.uniqueId).subscribe({
      next:  () => { this.loadVisitors(); this.cancelDelete(); },
      error: () => { this.cancelDelete(); }
    });
  }
  /**
   * Called when app-visitor-card emits (deleteVisitor).
   * The card handles its own confirmation modal, so here we call the API directly.
   */
  handleCardDelete(v: any): void {
    this.visitorService.deleteVisitor(v.uniqueId).subscribe({
      next:  () => this.loadVisitors(),
      error: (err) => console.error('Failed to delete visitor', err)
    });
  }
  // ─── Filtering ───────────────────────────────────────────────
  get filterVisitors(): any[] {
    const search = this.searchText?.toLowerCase().trim() || '';
    return this.visitors.filter(v => {
      const matchesSearch =
        v.name?.toLowerCase().includes(search) ||
        v.uniqueId?.toLowerCase().includes(search);
      let matchesDate = true;
      if (this.fromDate || this.toDate) {
        const visitDate = new Date(v.createdAt);
        visitDate.setHours(0, 0, 0, 0);
        if (this.fromDate) {
          const from = new Date(this.fromDate);
          from.setHours(0, 0, 0, 0);
          if (visitDate < from) matchesDate = false;
        }
        if (this.toDate) {
          const to = new Date(this.toDate);
          to.setHours(23, 59, 59, 999);
          if (visitDate > to) matchesDate = false;
        }
      }
      return matchesSearch && matchesDate;
    });
  }
  // ─── Pagination ──────────────────────────────────────────────
  get paginatedVisitors(): any[] {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filterVisitors.slice(start, start + this.itemsPerPage);
  }
  get totalPages(): number {
    return Math.ceil(this.filterVisitors.length / this.itemsPerPage) || 1;
  }
  nextPage(): void { if (this.currentPage < this.totalPages) this.currentPage++; }
  prevPage(): void { if (this.currentPage > 1)               this.currentPage--; }
}
