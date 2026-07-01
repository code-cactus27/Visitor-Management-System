import { Component, EventEmitter, Output, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { ThemeService } from 'src/app/core/services/theme.service';
import { HttpClient } from '@angular/common/http';
import { Subscription, interval } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { NotificationService } from 'src/app/core/services/notification.service';
interface ActivityLog {
  id: number;
  uniqueId: string;
  action: string;
  message: string;
  timestamp: string; // ISO format, parse if you need Date
}
@Component({
  selector: 'app-reception-navbar',
  templateUrl: './reception-navbar.component.html',
  styleUrls: ['./reception-navbar.component.css']
})
export class ReceptionNavbarComponent implements OnInit, OnDestroy {
  @Output() viewChange = new EventEmitter<string>();
  activeView: string = 'home';
  isSidebarOpen: boolean = true;
  logs: ActivityLog[] = [];
  logsLoading = false;
  logsError = '';
  logsVisible = true;
  private logsSubscription?: Subscription;
  constructor(
    private authService: AuthService,
    private router: Router,
    public themeService: ThemeService,
    private http: HttpClient,
    private notificationService: NotificationService
  ) { }
  ngOnInit(): void {
    // Start real-time polling for activity logs
    this.startPollingLogs();
  }
  ngOnDestroy(): void {
    if (this.logsSubscription) {
      this.logsSubscription.unsubscribe();
    }
  }
  private initialLoadDone = false;
  startPollingLogs() {
    this.logsSubscription = interval(5000) // Poll every 5 seconds
      .pipe(
        startWith(0),
        switchMap(() => {
          // Show loading only for the first fetch attempt
          if (!this.initialLoadDone) {
            this.logsLoading = true;
          }
          // Always fetch since it's now a persistent section
          return this.http.get<ActivityLog[]>('/gate/activity-logs');
        })
      )
      .subscribe({
        next: (logs) => {
          this.initialLoadDone = true;
          if (logs && Array.isArray(logs)) {
            // Sort logs by timestamp descending (latest first)
            this.logs = logs.sort((a, b) =>
              new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime()
            );
          }
          this.logsLoading = false;
        },
        error: (err) => {
          this.initialLoadDone = true;
          console.error('Failed to load activity logs', err);
          this.logsError = 'Failed to load activity logs';
          this.logsLoading = false;
        }
      });
  }
  navigateTo(view: string) {
    this.activeView = view;
    this.viewChange.emit(view);
    this.isSidebarOpen = false;
  }
  onLogout() {
    this.authService.logout().subscribe(
      () => this.router.navigate(['/login']),
      () => this.router.navigate(['/login'])
    );
  }
  getLogIcon(action: string): string {
    const a = action?.toLowerCase() || '';
    if (a.includes('check-in') || a.includes('approve')) return 'fa-door-open';
    if (a.includes('check-out')) return 'fa-door-closed';
    if (a.includes('register') || a.includes('add')) return 'fa-user-plus';
    if (a.includes('reject') || a.includes('expire')) return 'fa-user-times';
    return 'fa-info-circle';
  }
  getLogStatusClass(action: string): string {
    const a = action?.toLowerCase() || '';
    if (a.includes('check-in') || a.includes('approve')) return 'status-success';
    if (a.includes('check-out')) return 'status-info';
    if (a.includes('register') || a.includes('add')) return 'status-primary';
    if (a.includes('reject') || a.includes('expire')) return 'status-danger';
    return 'status-default';
  }
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  toggleNotifications() {
    this.notificationService.toggleDrawer();
  }
  @Output() feedbackClick = new EventEmitter<void>();
  openFeedback() {
    this.feedbackClick.emit();
  }
}
