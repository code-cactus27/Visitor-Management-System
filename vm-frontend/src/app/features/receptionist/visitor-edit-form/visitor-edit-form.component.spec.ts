import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VisitorEditFormComponent } from './visitor-edit-form.component';
describe('VisitorEditFormComponent', () => {
  let component: VisitorEditFormComponent;
  let fixture: ComponentFixture<VisitorEditFormComponent>;
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisitorEditFormComponent ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(VisitorEditFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
