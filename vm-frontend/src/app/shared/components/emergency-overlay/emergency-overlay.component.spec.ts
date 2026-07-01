import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmergencyOverlayComponent } from './emergency-overlay.component';

describe('EmergencyOverlayComponent', () => {
  let component: EmergencyOverlayComponent;
  let fixture: ComponentFixture<EmergencyOverlayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmergencyOverlayComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmergencyOverlayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
