import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { AuthService } from 'src/app/core/services/auth.service';
import { ThemeService } from 'src/app/core/services/theme.service';
interface NavItem {
  label: string;
  icon: string;
  route: string;
  fragment?: string;
}
@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  role: string = '';
  currentUrl = '';
  private readonly navItemsByRole: Record<string, NavItem[]> = {
    ROLE_RECEPTIONIST: [
      { label: 'Visitor Desk', icon: 'fa-solid fa-clipboard-list', route: '/receptionist' },
      { label: 'Register Visitor', icon: 'fa-solid fa-user-plus', route: '/receptionist', fragment: 'register' },
      { label: 'Visitor Records', icon: 'fa-solid fa-address-book', route: '/receptionist', fragment: 'records' }
    ],
    ROLE_SECURITY: [
      { label: 'Dashboard', icon: 'fa-solid fa-gauge-high', route: '/security' },
      { label: 'Search Visitors', icon: 'fa-solid fa-magnifying-glass', route: '/security', fragment: 'search' },
      { label: 'Visitor Status', icon: 'fa-solid fa-shield-halved', route: '/security', fragment: 'visitors' }
    ],
    ROLE_ADMIN: [
      { label: 'Admin Dashboard', icon: 'fa-solid fa-users-gear', route: '/admin' }
    ]
  };
  constructor(
    private auth: AuthService,
    private router: Router,
    public themeService: ThemeService
  ) {
  }
  ngOnInit(): void {
    this.role = this.auth.getRole();
    this.currentUrl = this.router.url;
    this.syncRoleFromUrl();
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe(event => {
        this.currentUrl = event.urlAfterRedirects;
        this.role = this.auth.getRole();
        this.syncRoleFromUrl();
      });
  }
  get navItems(): NavItem[] {
    return this.navItemsByRole[this.role] || [];
  }
  get showSidebar(): boolean {
    return this.navItems.length > 0;
  }
  get roleTitle(): string {
    const titles: Record<string, string> = {
      ROLE_RECEPTIONIST: 'Receptionist',
      ROLE_SECURITY: 'Security Officer',
      ROLE_ADMIN: 'System Admin'
    };
    return titles[this.role] || 'Visitor Management';
  }
  isActive(item: NavItem): boolean {
    const [path, fragment] = this.currentUrl.split('#');
    if (item.fragment) {
      return path === item.route && fragment === item.fragment;
    }
    return (path === item.route || path.startsWith(`${item.route}/`)) && !fragment;
  }
  private syncRoleFromUrl(): void {
    if (this.role) {
      return;
    }
    if (this.currentUrl.startsWith('/receptionist')) {
      this.role = 'ROLE_RECEPTIONIST';
    } else if (this.currentUrl.startsWith('/security') || this.currentUrl.startsWith('/securityOfficer')) {
      this.role = 'ROLE_SECURITY';
    } else if (this.currentUrl.startsWith('/admin') || this.currentUrl.startsWith('/systemAdministrator')) {
      this.role = 'ROLE_ADMIN';
    }
  }
}
