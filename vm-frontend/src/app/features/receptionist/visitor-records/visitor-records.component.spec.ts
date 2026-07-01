import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VisitorRecordsComponent } from './visitor-records.component';
describe('VisitorRecordsComponent', () => {
  let component: VisitorRecordsComponent;
  let fixture: ComponentFixture<VisitorRecordsComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorRecordsComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(VisitorRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
