import { Component, OnInit } from '@angular/core';
import { AuthService } from './core/services/auth.service';
import { NavigationEnd, Router } from '@angular/router';
import { ThemeService } from './core/services/theme.service';
import { filter } from 'rxjs';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'FrontEnd';
  hasWorkspaceNav = false;
  constructor(private themeService: ThemeService){
  }
  ngOnInit() {
    this.themeService.loadTheme();
  }
  isCheckingAuth=false;
}
