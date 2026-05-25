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
  @Input() icon: string = '';
  @Input() isCurrency: boolean = false;
  @Input() highlight: boolean = false;
}
