import { Component, OnDestroy, OnInit } from '@angular/core';
import { ThemeService } from 'src/app/core/services/theme.service';
import { VisitorService } from 'src/app/core/services/visitor.service';
import { Subscription, timer } from 'rxjs';
import { switchMap } from 'rxjs/operators';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  dashboardData: any = {};
  private refreshSub?: Subscription;
  refreshIntervalMs = 500;
  constructor(private visitorService: VisitorService, public themeService: ThemeService) {}
  ngOnInit(): void {
    this.refreshSub = timer(0, this.refreshIntervalMs)
      .pipe(switchMap(() => this.visitorService.getDashboard()))
      .subscribe({
        next: (data) => {
          this.dashboardData = data;
        },
        error: (err) => console.error(err)
      });
  }
  ngOnDestroy(): void {
    this.refreshSub?.unsubscribe();
  }
}
