import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SecurityOfficerComponent } from './security-officer.component';
describe('SecurityOfficerComponent', () => {
  let component: SecurityOfficerComponent;
  let fixture: ComponentFixture<SecurityOfficerComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SecurityOfficerComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(SecurityOfficerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
