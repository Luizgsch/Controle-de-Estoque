import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Produto } from '../models/produto';
import { ApiResponse } from '../models/api-response';

@Injectable({
  providedIn: 'root'
})
export class ProductsService {
  private apiUrl = 'http://localhost:8080/api/produtos';

  private productsSubject = new BehaviorSubject<Produto[]>([]);
  public products$ = this.productsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  private totalElementsSubject = new BehaviorSubject<number>(0);
  public totalElements$ = this.totalElementsSubject.asObservable();

  private totalPagesSubject = new BehaviorSubject<number>(0);
  public totalPages$ = this.totalPagesSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadProducts(page: number = 0, pageSize: number = 10): Observable<ApiResponse<Produto[]>> {
    this.loadingSubject.next(true);
    return this.http.get<ApiResponse<Produto[]>>(
      `${this.apiUrl}?page=${page}&size=${pageSize}`
    ).pipe(
      tap(response => {
        this.productsSubject.next(response.content);
        this.totalElementsSubject.next(response.totalElements ?? 0);
        this.totalPagesSubject.next(response.totalPages ?? 0);
        this.loadingSubject.next(false);
      })
    );
  }

  searchProducts(term: string): Observable<Produto[]> {
    this.loadingSubject.next(true);
    return this.http.get<Produto[]>(
      `${this.apiUrl}/buscar/termo?termo=${encodeURIComponent(term)}`
    ).pipe(
      tap(produtos => {
        this.productsSubject.next(Array.isArray(produtos) ? produtos : []);
        this.loadingSubject.next(false);
      })
    );
  }

  createProduct(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(this.apiUrl, produto);
  }

  updateProduct(id: number, produto: Produto): Observable<Produto> {
    return this.http.put<Produto>(`${this.apiUrl}/${id}`, produto);
  }

  updateStock(id: number, quantity: number): Observable<Produto> {
    return this.http.patch<Produto>(`${this.apiUrl}/${id}/estoque?quantidade=${quantity}`, {});
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  loadLowStockAlerts(): Observable<Produto[]> {
    return this.http.get<Produto[]>(`${this.apiUrl}/alertas/estoque-minimo`);
  }

  getTotalInventoryValue(): number {
    return this.productsSubject.value.reduce(
      (sum, p) => sum + (p.preco * p.quantidade), 0
    );
  }
}
