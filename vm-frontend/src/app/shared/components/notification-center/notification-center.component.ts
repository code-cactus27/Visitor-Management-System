import { Component, OnInit, OnDestroy } from '@angular/core';
import { NotificationService, AppNotification } from 'src/app/core/services/notification.service';
import { Subscription } from 'rxjs';
@Component({
  selector: 'app-notification-center',
  templateUrl: './notification-center.component.html',
  styleUrls: ['./notification-center.component.css'],
})
export class NotificationCenterComponent implements OnInit, OnDestroy {
  activeNotifications: AppNotification[] = [];
  history: AppNotification[] = [];
  isDrawerOpen = false;
  private subs: Subscription[] = [];
  constructor(private notificationService: NotificationService) { }
  ngOnInit() {
    this.subs.push(this.notificationService.notifications$.subscribe(note => {
      this.activeNotifications.push(note);
      setTimeout(() => this.removeNotification(note.id), 5000);
    }));
    this.subs.push(this.notificationService.history$.subscribe(hist => {
      this.history = hist;
    }));
    this.subs.push(this.notificationService.drawerOpen$.subscribe(open => {
      this.isDrawerOpen = open;
    }));
    // Initial history
    this.history = this.notificationService.getHistory();
  }
  removeNotification(id: number) {
    this.activeNotifications = this.activeNotifications.filter(n => n.id !== id);
  }
  clearAll() {
    this.notificationService.clearHistory();
  }
  toggleDrawer() {
    this.notificationService.toggleDrawer();
  }
  ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
  }
}
