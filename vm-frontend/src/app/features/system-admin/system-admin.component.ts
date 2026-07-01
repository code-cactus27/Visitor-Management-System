import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router } from '@angular/router';
import { LoaderService } from 'src/app/core/services/loader.service';
import { ThemeService } from 'src/app/core/services/theme.service';
@Component({
  selector: 'app-system-admin',
  templateUrl: './system-admin.component.html',
  styleUrls: ['./system-admin.component.css'],
})
export class SystemAdminComponent implements OnInit {
  isLoading=false;
  constructor(
    private router:Router,
    private loader:LoaderService,
    private cd :ChangeDetectorRef,
    public themeService:ThemeService
  ){
  }
  ngOnInit(): void {
    this.loader.loadings$.subscribe(state=>{
      this.isLoading=state;
      this.cd.detectChanges();
    });
    this.router.events.subscribe(event=>{
      if(event instanceof NavigationStart){
        this.loader.show()
      }
      if(
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ){
        this.loader.hide();
      }
      this.cd.detectChanges();
    });
  }
  config={
    defaultDuration:4,
    allowedDurations:'1,2,4,8',
    email:'security@company.com'
  };
}
