import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
export interface AppNotification {
  id: number;
  type: 'info' | 'success' | 'warning' | 'error';
  title: string;
  message: string;
  timestamp: Date;
}
@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new Subject<AppNotification>();
  notifications$ = this.notificationSubject.asObservable();
  private history: AppNotification[] = [];
  private historySubject = new Subject<AppNotification[]>();
  history$ = this.historySubject.asObservable();
  private drawerOpen = false;
  private drawerSubject = new Subject<boolean>();
  drawerOpen$ = this.drawerSubject.asObservable();
  private counter = 0;
  show(title: string, message: string, type: 'info' | 'success' | 'warning' | 'error' = 'info') {
    const notification: AppNotification = {
      id: ++this.counter,
      type,
      title,
      message,
      timestamp: new Date()
    };
    this.notificationSubject.next(notification);
    this.history.unshift(notification); // Add to history
    this.historySubject.next([...this.history]);
  }
  success(title: string, message: string) {
    this.show(title, message, 'success');
  }
  info(title: string, message: string) {
    this.show(title, message, 'info');
  }
  warning(title: string, message: string) {
    this.show(title, message, 'warning');
  }
  error(title: string, message: string) {
    this.show(title, message, 'error');
  }
  notifyCheckIn(visitorName: string, uniqueId: string) {
    this.show(
      'New Check-in',
      `Visitor ${visitorName} (${uniqueId}) has just checked in.`,
      'success'
    );
    // You could also trigger a sound here
    this.playNotificationSound();
  }
  toggleDrawer() {
    this.drawerOpen = !this.drawerOpen;
    this.drawerSubject.next(this.drawerOpen);
  }
  getHistory() {
    return [...this.history];
  }
  clearHistory() {
    this.history = [];
    this.historySubject.next([]);
  }
  private playNotificationSound() {
    try {
      const audio = new Audio('assets/notification.mp3');
      audio.play().catch(e => console.log('Audio play failed', e));
    } catch (e) {
      console.log('Audio play failed', e);
    }
  }
}
