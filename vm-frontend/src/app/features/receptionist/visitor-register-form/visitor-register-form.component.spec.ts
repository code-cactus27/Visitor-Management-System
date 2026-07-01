import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VisitorRegisterFormComponent } from './visitor-register-form.component';
describe('VisitorRegisterFormComponent', () => {
  let component: VisitorRegisterFormComponent;
  let fixture: ComponentFixture<VisitorRegisterFormComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorRegisterFormComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(VisitorRegisterFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
