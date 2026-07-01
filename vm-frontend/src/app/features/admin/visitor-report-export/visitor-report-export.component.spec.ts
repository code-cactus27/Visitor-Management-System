import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VisitorReportExportComponent } from './visitor-report-export.component';
describe('VisitorReportExportComponent', () => {
  let component: VisitorReportExportComponent;
  let fixture: ComponentFixture<VisitorReportExportComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorReportExportComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(VisitorReportExportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
