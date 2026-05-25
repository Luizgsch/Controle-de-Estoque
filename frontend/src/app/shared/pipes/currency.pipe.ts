import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appCurrency',
  standalone: true
})
export class AppCurrencyPipe implements PipeTransform {
  transform(value: number | null | undefined): string {
    if (value === null || value === undefined) return 'R$ 0,00';

    const isNegative = value < 0;
    const absoluteValue = Math.abs(value);
    const formatted = absoluteValue.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });

    return isNegative ? `-R$ ${formatted}` : `R$ ${formatted}`;
  }
}
