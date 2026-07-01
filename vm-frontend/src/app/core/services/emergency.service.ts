import { Injectable, NgZone } from '@angular/core'; // 1. Added NgZone import
import { BehaviorSubject } from 'rxjs';

export interface EmergencyAlert {
  type:     'fire' | 'earthquake' | 'medical' | 'evacuation' |
    'security' | 'hazmat' | 'power' | 'bomb' | 'custom';
  title:    string;
  message:  string;
  icon:     string;
  color:    string;
  active:   boolean;
  raisedAt: Date | null;
}

export const EMERGENCY_TYPES: Omit<EmergencyAlert, 'active' | 'raisedAt'>[] = [
  { type: 'fire', title: 'FIRE ALERT', message: 'Fire detected on the premises! Evacuate immediately via the nearest exit. Do NOT use lifts.', icon: 'fa-fire', color: 'red' },
  { type: 'earthquake', title: 'EARTHQUAKE WARNING', message: 'Earthquake detected! Drop, cover and hold on. Move away from windows and exterior walls.', icon: 'fa-mountain', color: 'orange' },
  { type: 'medical', title: 'MEDICAL EMERGENCY', message: 'Medical emergency in progress. Please clear the area immediately. First-aid team is responding.', icon: 'fa-heart-pulse', color: 'blue' },
  { type: 'evacuation', title: 'EVACUATION ORDER', message: 'Mandatory evacuation in effect. Proceed calmly to the nearest emergency exit and assembly point.', icon: 'fa-person-running', color: 'amber' },
  { type: 'security', title: 'SECURITY BREACH', message: 'Security breach detected. Lock down your area and await further instructions from security personnel.', icon: 'fa-shield-exclamation', color: 'purple' },
  { type: 'hazmat', title: 'HAZMAT INCIDENT', message: 'Hazardous material detected. Do not touch any suspicious substances. Move upwind and await instructions.', icon: 'fa-biohazard', color: 'orange' },
  { type: 'power', title: 'POWER OUTAGE', message: 'Power failure in progress. Stay calm, use emergency lighting and await restoration or evacuation order.', icon: 'fa-bolt', color: 'amber' },
  { type: 'bomb', title: 'BOMB THREAT', message: 'Bomb threat received. Do not touch any suspicious objects. Evacuate immediately and contact authorities.', icon: 'fa-skull-crossbones', color: 'red' },
  { type: 'custom', title: 'EMERGENCY ALERT', message: '', icon: 'fa-triangle-exclamation', color: 'red' }
];

@Injectable({ providedIn: 'root' })
export class EmergencyService {

  readonly emergencyTypes = EMERGENCY_TYPES;

  private defaultState: EmergencyAlert = {
    type:     'fire',
    title:    '',
    message:  '',
    icon:     '',
    color:    'red',
    active:   false,
    raisedAt: null
  };

  private alertSubject = new BehaviorSubject<EmergencyAlert>({ ...this.defaultState });
  alert$ = this.alertSubject.asObservable();

  private syncChannel = new BroadcastChannel('emergency_broadcast');

  // 2. Inject NgZone into the constructor
  constructor(private zone: NgZone) {
    this.syncChannel.onmessage = (event) => {
      if (event.data) {
        const alertData = event.data as EmergencyAlert;

        if (alertData.raisedAt) {
          alertData.raisedAt = new Date(alertData.raisedAt);
        }

        // 3. CRITICAL: Force Angular to run change detection for this cross-tab event
        this.zone.run(() => {
          this.alertSubject.next(alertData);
        });
      }
    };
  }

  get current(): EmergencyAlert {
    return this.alertSubject.getValue();
  }

  raise(type: EmergencyAlert['type']): void {
    const template = EMERGENCY_TYPES.find(t => t.type === type);
    if (!template) return;

    const newState: EmergencyAlert = { ...template, active: true, raisedAt: new Date() };
    this.alertSubject.next(newState);
    this.syncChannel.postMessage(newState);
  }

  raiseCustom(reason: string): void {
    const template = EMERGENCY_TYPES.find(t => t.type === 'custom')!;
    const newState: EmergencyAlert = {
      ...template,
      title:    'EMERGENCY ALERT',
      message:  reason.trim() || 'Emergency situation reported. Follow security instructions immediately.',
      active:   true,
      raisedAt: new Date()
    };

    this.alertSubject.next(newState);
    this.syncChannel.postMessage(newState);
  }

  dismiss(): void {
    const clearedState = { ...this.defaultState };
    this.alertSubject.next(clearedState);
    this.syncChannel.postMessage(clearedState);
  }
}















