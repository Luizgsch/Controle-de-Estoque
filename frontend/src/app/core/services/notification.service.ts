import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notification {
  type: 'success' | 'error' | 'info';
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notificationSubject = new BehaviorSubject<Notification | null>(null);
  public notification$ = this.notificationSubject.asObservable();
  private timeoutId: any;

  showSuccess(message: string): void {
    this.show({ type: 'success', message });
  }

  showError(message: string): void {
    this.show({ type: 'error', message });
  }

  showInfo(message: string): void {
    this.show({ type: 'info', message });
  }

  clear(): void {
    this.notificationSubject.next(null);
    if (this.timeoutId) clearTimeout(this.timeoutId);
  }

  private show(notification: Notification): void {
    this.notificationSubject.next(notification);
    if (this.timeoutId) clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.clear(), 5000);
  }
}
