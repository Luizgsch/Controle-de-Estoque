import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ProductsService } from '../../core/services/products.service';
import { MetricCardComponent } from '../../shared/components/metric-card/metric-card.component';
import { Produto } from '../../core/models/produto';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, MetricCardComponent],
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  totalValue = 0;
  totalItems = 0;
  lowStockCount = 0;
  loading = true;

  constructor(private productsService: ProductsService) {}

  ngOnInit(): void {
    this.productsService.loadProducts(0, 100).subscribe({
      next: (response) => {
        const products: Produto[] = response.content;
        this.totalItems = products.reduce((sum, p) => sum + p.quantidade, 0);
        this.totalValue = products.reduce((sum, p) => sum + (p.preco * p.quantidade), 0);
        this.loading = false;
      },
      error: () => { this.loading = false; }
    });

    this.productsService.loadLowStockAlerts().subscribe({
      next: (alerts) => { this.lowStockCount = alerts.length; },
      error: () => { this.lowStockCount = 0; }
    });
  }
}
