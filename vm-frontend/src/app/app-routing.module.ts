import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from './features/auth/login/login.component';
import {ForgotPasswordComponent} from './features/auth/forgot-password/forgot-password.component';
import {ResetPasswordComponent} from './features/auth/reset-password/reset-password.component';

import {ReceptionistComponent} from './features/receptionist/receptionist.component';
import {VisitorEditFormComponent} from './features/receptionist/visitor-edit-form/visitor-edit-form.component';

import {SystemAdminComponent} from './features/system-admin/system-admin.component';
import {HomeComponent} from './features/admin/home/home.component';
import {UserManagementComponent} from './features/admin/user-management/user-management.component';
import {ReportDashboardComponent} from './features/admin/report-dashboard/report-dashboard.component';
import {VisitorReportExportComponent} from './features/admin/visitor-report-export/visitor-report-export.component';
import {ConfigComponent} from './features/admin/config/config.component';

import {SecurityOfficerComponent} from './features/security-officer/security-officer.component';
import {DashboardComponent} from './features/security-officer/dashboard/dashboard.component';
import {SearchComponent} from './features/security-officer/search/search.component';
import {GatepassComponent} from './features/security-officer/gatepass/gatepass.component';

import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {roleGuard} from './core/guards/role.guard';
import {VisitorTableComponent} from "./features/security-officer/visitor-table/visitor-table.component";

const routes: Routes = [

  {path: '', redirectTo: 'login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'reset-password', component: ResetPasswordComponent},

  {path: 'visitor-edit/:uniqueId', component: VisitorEditFormComponent},
  {
    path: 'receptionist',
    component: ReceptionistComponent,
    canActivate: [roleGuard],
    data: {role: 'RECEPTIONIST'}
  },

  {
    path: 'admin',
    component: SystemAdminComponent,
    canActivate: [roleGuard],
    canActivateChild: [roleGuard],
    data: {role: 'ADMIN'},
    children: [
      {path: '', component: HomeComponent},
      {path: 'users', component: UserManagementComponent},
      {path: 'reports', component: ReportDashboardComponent},
      {path: 'export_csv', component: VisitorReportExportComponent},
      {path: 'settings', component: ConfigComponent}
    ]
  },

  {
    path: 'security',
    component: SecurityOfficerComponent,
    canActivate: [roleGuard],
    data: {role: 'SECURITY'},
    children: [
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'},
      {path: 'visitors', component: VisitorTableComponent},
      {path: 'dashboard', component: DashboardComponent},
      {path: 'search', component: SearchComponent},
      {path: 'gatepass/:visitorId/visit/:visitId', component: GatepassComponent}
    ]
  },

  // ── Catch-all ─────────────────────────────────────────────
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
