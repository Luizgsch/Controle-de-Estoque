import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { ProductsService } from '../../core/services/products.service';
import { NotificationService } from '../../core/services/notification.service';
import { Produto } from '../../core/models/produto';
import { AppCurrencyPipe } from '../../shared/pipes/currency.pipe';
import { ProductFormModalComponent } from './components/product-form-modal/product-form-modal.component';
import { StockAdjustmentModalComponent } from './components/stock-adjustment-modal/stock-adjustment-modal.component';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    AppCurrencyPipe,
    ProductFormModalComponent,
    StockAdjustmentModalComponent,
  ],
  templateUrl: './products.component.html',
})
export class ProductsComponent implements OnInit, OnDestroy {
  products: Produto[] = [];
  loading = false;
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  pageSize = 10;

  searchControl = new FormControl('');
  isSearching = false;

  showFormModal = false;
  selectedProduct: Produto | null = null;

  showStockModal = false;
  stockProduct: Produto | null = null;

  deleteConfirmId: number | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private productsService: ProductsService,
    private notification: NotificationService
  ) {}

  ngOnInit(): void {
    this.productsService.products$.pipe(takeUntil(this.destroy$))
      .subscribe(p => this.products = p);

    this.productsService.loading$.pipe(takeUntil(this.destroy$))
      .subscribe(l => this.loading = l);

    this.productsService.totalPages$.pipe(takeUntil(this.destroy$))
      .subscribe(t => this.totalPages = t);

    this.productsService.totalElements$.pipe(takeUntil(this.destroy$))
      .subscribe(t => this.totalElements = t);

    this.loadPage(0);

    this.searchControl.valueChanges.pipe(
      debounceTime(350),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(term => {
      if (term && term.trim().length > 0) {
        this.isSearching = true;
        this.productsService.searchProducts(term.trim()).subscribe();
      } else {
        this.isSearching = false;
        this.loadPage(0);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadPage(page: number): void {
    this.currentPage = page;
    this.productsService.loadProducts(page, this.pageSize).subscribe();
  }

  openCreateModal(): void {
    this.selectedProduct = null;
    this.showFormModal = true;
  }

  openEditModal(produto: Produto): void {
    this.selectedProduct = { ...produto };
    this.showFormModal = true;
  }

  onFormSave(produto: Produto): void {
    if (produto.id) {
      this.productsService.updateProduct(produto.id, produto).subscribe({
        next: () => {
          this.showFormModal = false;
          this.notification.showSuccess('Produto atualizado com sucesso');
          this.loadPage(this.currentPage);
        },
        error: () => {}
      });
    } else {
      this.productsService.createProduct(produto).subscribe({
        next: () => {
          this.showFormModal = false;
          this.notification.showSuccess('Produto criado com sucesso');
          this.loadPage(0);
        },
        error: () => {}
      });
    }
  }

  onFormCancel(): void {
    this.showFormModal = false;
    this.selectedProduct = null;
  }

  openStockModal(produto: Produto): void {
    this.stockProduct = produto;
    this.showStockModal = true;
  }

  onStockConfirm(quantity: number): void {
    if (!this.stockProduct?.id) return;
    this.productsService.updateStock(this.stockProduct.id, quantity).subscribe({
      next: () => {
        this.showStockModal = false;
        this.stockProduct = null;
        this.notification.showSuccess('Estoque atualizado com sucesso');
        this.loadPage(this.currentPage);
      },
      error: () => { this.showStockModal = false; }
    });
  }

  onStockCancel(): void {
    this.showStockModal = false;
    this.stockProduct = null;
  }

  requestDelete(id: number): void {
    this.deleteConfirmId = id;
  }

  confirmDelete(): void {
    if (this.deleteConfirmId === null) return;
    this.productsService.deleteProduct(this.deleteConfirmId).subscribe({
      next: () => {
        this.deleteConfirmId = null;
        this.notification.showSuccess('Produto excluído com sucesso');
        this.loadPage(this.currentPage);
      },
      error: () => { this.deleteConfirmId = null; }
    });
  }

  cancelDelete(): void {
    this.deleteConfirmId = null;
  }

  isLowStock(produto: Produto): boolean {
    return produto.quantidade <= produto.quantidadeMinima;
  }
}
