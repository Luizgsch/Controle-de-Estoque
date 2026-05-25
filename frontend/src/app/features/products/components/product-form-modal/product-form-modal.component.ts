import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Produto } from '../../../../core/models/produto';

@Component({
  selector: 'app-product-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './product-form-modal.component.html',
})
export class ProductFormModalComponent implements OnInit {
  @Input() produto: Produto | null = null;
  @Output() save = new EventEmitter<Produto>();
  @Output() cancel = new EventEmitter<void>();

  form!: FormGroup;
  loading = false;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nome: [this.produto?.nome ?? '', Validators.required],
      sku: [this.produto?.sku ?? '', Validators.required],
      categoria: [this.produto?.categoria ?? ''],
      preco: [this.produto?.preco ?? '', [Validators.required, Validators.min(0.01)]],
      quantidade: [this.produto?.quantidade ?? 0, [Validators.required, Validators.min(0)]],
      quantidadeMinima: [this.produto?.quantidadeMinima ?? 0, [Validators.required, Validators.min(0)]],
      descricao: [this.produto?.descricao ?? ''],
    });
  }

  get isEdit(): boolean { return !!this.produto?.id; }
  get nome() { return this.form.get('nome')!; }
  get sku() { return this.form.get('sku')!; }
  get preco() { return this.form.get('preco')!; }
  get quantidade() { return this.form.get('quantidade')!; }
  get quantidadeMinima() { return this.form.get('quantidadeMinima')!; }

  onSave(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const data: Produto = { ...this.form.value };
    if (this.produto?.id) data.id = this.produto.id;
    this.save.emit(data);
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
