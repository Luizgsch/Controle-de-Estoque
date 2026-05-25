import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppCurrencyPipe } from '../../pipes/currency.pipe';

@Component({
  selector: 'app-metric-card',
  standalone: true,
  imports: [CommonModule, AppCurrencyPipe],
  templateUrl: './metric-card.component.html',
})
export class MetricCardComponent {
  @Input() title: string = '';
  @Input() value: string | number = 0;
  @Input() isCurrency: boolean = false;
  @Input() colorScheme: 'indigo' | 'emerald' | 'amber' = 'indigo';
  @Input() iconType: 'currency' | 'box' | 'alert' = 'box';

  get borderClass(): string {
    return {
      indigo: 'border-l-indigo-500',
      emerald: 'border-l-emerald-500',
      amber: 'border-l-amber-500',
    }[this.colorScheme];
  }

  get iconBgClass(): string {
    return {
      indigo: 'bg-indigo-50 text-indigo-600',
      emerald: 'bg-emerald-50 text-emerald-600',
      amber: 'bg-amber-50 text-amber-600',
    }[this.colorScheme];
  }

  get valueColorClass(): string {
    return {
      indigo: 'text-slate-900',
      emerald: 'text-slate-900',
      amber: 'text-amber-600',
    }[this.colorScheme];
  }
}
