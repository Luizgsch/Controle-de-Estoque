import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Produto } from '../../../../core/models/produto';

@Component({
  selector: 'app-stock-adjustment-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './stock-adjustment-modal.component.html',
})
export class StockAdjustmentModalComponent {
  @Input() produto!: Produto;
  @Output() confirm = new EventEmitter<number>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      quantidade: [0, [Validators.required]],
    });
  }

  get quantidade() { return this.form.get('quantidade')!; }

  get newStock(): number {
    return (this.produto?.quantidade ?? 0) + (Number(this.quantidade.value) || 0);
  }

  onConfirm(): void {
    if (this.form.invalid) return;
    this.confirm.emit(Number(this.quantidade.value));
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
