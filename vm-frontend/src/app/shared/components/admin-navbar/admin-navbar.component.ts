import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth.service';
import { ThemeService } from 'src/app/core/services/theme.service';
import { NotificationService } from 'src/app/core/services/notification.service';
@Component({
  selector: 'app-admin-navbar',
  templateUrl: './admin-navbar.component.html',
  styleUrls: ['./admin-navbar.component.css']
})
export class AdminNavbarComponent {
  isSidebarOpen = false;
  constructor(
    private authService: AuthService,
    private router: Router,
    public themeService: ThemeService,
    private notificationService: NotificationService
  ) {}
  toggleNotifications() {
    this.notificationService.toggleDrawer();
  }
  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }
  onLogout(){
    this.authService.logout().subscribe((res)=>{
      this.router.navigate(['/login']);
    },(err)=>{
      this.router.navigate(['/login']);
    })
  }
}
