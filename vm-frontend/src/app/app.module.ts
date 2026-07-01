import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReceptionistComponent } from './features/receptionist/receptionist.component';
import { SecurityOfficerComponent } from './features/security-officer/security-officer.component';
import { SystemAdminComponent } from './features/system-admin/system-admin.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { LoginComponent } from './features/auth/login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientXsrfModule } from '@angular/common/http';
import { VisitorRecordsComponent } from './features/receptionist/visitor-records/visitor-records.component';
import { ContactPipe } from './shared/Pipes/contact.pipe';
import { VisitorRegisterFormComponent } from './features/receptionist/visitor-register-form/visitor-register-form.component';
import { VisitorEditFormComponent } from './features/receptionist/visitor-edit-form/visitor-edit-form.component';
import { SearchComponent } from './features/security-officer/search/search.component';
import { VisitorTableComponent } from './features/security-officer/visitor-table/visitor-table.component';
import { DashboardComponent } from './features/security-officer/dashboard/dashboard.component';
import { GatepassComponent } from './features/security-officer/gatepass/gatepass.component';
import { ConfigComponent } from './features/admin/config/config.component';
import { HomeComponent } from './features/admin/home/home.component';
import { ReportDashboardComponent } from './features/admin/report-dashboard/report-dashboard.component';
import { UserManagementComponent } from './features/admin/user-management/user-management.component';
import { VisitorReportExportComponent } from './features/admin/visitor-report-export/visitor-report-export.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { SecurityNavbarComponent } from './shared/components/security-navbar/security-navbar.component';
import { AdminNavbarComponent } from './shared/components/admin-navbar/admin-navbar.component';
import { ReceptionNavbarComponent } from './shared/components/reception-navbar/reception-navbar.component';
import { NotificationCenterComponent } from './shared/components/notification-center/notification-center.component';
import { FeedbackFormComponent } from './features/receptionist/feedback-form/feedback-form.component';
import { VisitorCardComponent } from './features/receptionist/visitor-card/visitor-card.component';
import { ForgotPasswordComponent } from './features/auth/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './features/auth/reset-password/reset-password.component';
import { EmergencyOverlayComponent } from './shared/components/emergency-overlay/emergency-overlay.component';

@NgModule({
  declarations: [
    AppComponent,
    ReceptionistComponent,
    SecurityOfficerComponent,
    SystemAdminComponent,
    NavbarComponent,
    LoginComponent,
    VisitorRecordsComponent,
    ContactPipe,
    VisitorRegisterFormComponent,
    VisitorEditFormComponent,
    SearchComponent,
    VisitorTableComponent,
    DashboardComponent,
    ConfigComponent,
    HomeComponent,
    ReportDashboardComponent,
    UserManagementComponent,
    VisitorReportExportComponent,
    PageNotFoundComponent,
    SecurityNavbarComponent,
    AdminNavbarComponent,
    ReceptionNavbarComponent,
    NotificationCenterComponent,
    FeedbackFormComponent,
    VisitorCardComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    EmergencyOverlayComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    HttpClientXsrfModule.withOptions({
      cookieName: 'XSRF-TOKEN',
      headerName: 'X-XSRF-TOKEN'
    }),
    GatepassComponent
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
