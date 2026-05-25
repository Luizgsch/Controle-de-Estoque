# Frontend Angular SPA Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a complete Angular SPA frontend for inventory management with authentication, responsive UI, product CRUD, and dashboard metrics.

**Architecture:** Feature modules (Login, Dashboard, Products) with shared components + Core module for global services. RxJS Observables for state, Tailwind CSS for styling, Reactive Forms for validation.

**Tech Stack:** Angular 18+, Tailwind CSS, RxJS, Reactive Forms, Angular Router with Lazy Loading, HttpClient + Interceptors

---

## Phase 1: Project Setup & Configuration

### Task 1: Create Angular Project with Tailwind CSS

**Files:**
- Create: `frontend/` (new folder at project root)
- Modify: `frontend/package.json`
- Modify: `frontend/tailwind.config.js`
- Modify: `frontend/src/styles/tailwind.css`

- [ ] **Step 1: Create new Angular project**

```bash
cd /home/luiz/Documentos/controle-estoque
ng new frontend --routing --style=scss --skip-git
cd frontend
```

Expected: New Angular project scaffolded with routing enabled, SCSS support.

- [ ] **Step 2: Install Tailwind CSS**

```bash
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p
```

Expected: `tailwind.config.js` and `postcss.config.js` created.

- [ ] **Step 3: Configure Tailwind to process Angular templates**

Edit `frontend/tailwind.config.js`:

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

- [ ] **Step 4: Replace global styles with Tailwind**

Edit `frontend/src/styles.scss`:

```scss
@tailwind base;
@tailwind components;
@tailwind utilities;

// Custom utilities for light theme
@layer base {
  body {
    @apply bg-white text-gray-900;
  }
}
```

- [ ] **Step 5: Test Tailwind works**

Run: `ng serve`

Navigate to `http://localhost:4200`. No errors in console. Page loads.

- [ ] **Step 6: Commit**

```bash
cd frontend
git add .
git commit -m "feat: initialize Angular project with Tailwind CSS

- Create Angular 18+ SPA with routing and SCSS support
- Configure Tailwind CSS with PostCSS
- Set up global styles with Tailwind directives
- Ready for component development

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 2: Create Project Structure & Base App Component

**Files:**
- Modify: `frontend/src/app/app.component.ts`
- Modify: `frontend/src/app/app.component.html`
- Create: `frontend/src/app/core/`
- Create: `frontend/src/app/shared/`
- Create: `frontend/src/app/features/`

- [ ] **Step 1: Create folder structure**

```bash
mkdir -p src/app/core/{services,guards,interceptors,models}
mkdir -p src/app/shared/{components,pipes}
mkdir -p src/app/features/{login,dashboard,products}
```

- [ ] **Step 2: Update AppComponent to be a layout shell**

Edit `frontend/src/app/app.component.ts`:

```typescript
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: false,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Controle de Estoque';
}
```

- [ ] **Step 3: Update AppComponent template**

Edit `frontend/src/app/app.component.html`:

```html
<div class="flex flex-col h-screen">
  <!-- Router outlet for pages -->
  <router-outlet></router-outlet>
</div>
```

- [ ] **Step 4: Create AppModule to bootstrap**

Edit `frontend/src/app/app.module.ts`:

```typescript
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

- [ ] **Step 5: Verify structure compiles**

Run: `ng build`

Expected: No errors. Build succeeds.

- [ ] **Step 6: Commit**

```bash
git add src/app
git commit -m "feat: create project folder structure and app layout

- Set up core, shared, features module directories
- Create AppComponent as layout shell
- Configure AppModule with HttpClientModule
- Project ready for service and component development

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

## Phase 2: Core Services & Models

### Task 3: Create Data Models (Produto, Auth, API Response)

**Files:**
- Create: `frontend/src/app/core/models/produto.ts`
- Create: `frontend/src/app/core/models/auth.ts`
- Create: `frontend/src/app/core/models/api-response.ts`

- [ ] **Step 1: Create Produto model**

Create `frontend/src/app/core/models/produto.ts`:

```typescript
export interface Produto {
  id?: number;
  nome: string;
  sku: string;
  categoria: string;
  preco: number;
  quantidade: number;
  quantidadeMinima: number;
  descricao?: string;
  dataCriacao?: string;
  dataAtualizacao?: string;
}
```

- [ ] **Step 2: Create Auth models**

Create `frontend/src/app/core/models/auth.ts`:

```typescript
export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  tokenType: string;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface User {
  id: number;
  email: string;
  name: string;
}
```

- [ ] **Step 3: Create API Response wrapper**

Create `frontend/src/app/core/models/api-response.ts`:

```typescript
export interface ApiResponse<T> {
  content: T;
  page?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
  last?: boolean;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
```

- [ ] **Step 4: Verify models are importable**

Run TypeScript check:

```bash
npx tsc --noEmit
```

Expected: No errors.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/models
git commit -m "feat: create data models for Produto, Auth, and API responses

- Define Produto interface with all required fields
- Create LoginRequest, LoginResponse, User auth models
- Define ApiResponse wrapper and ApiError types
- Ready for service layer development

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 4: Create API Service (HTTP wrapper)

**Files:**
- Create: `frontend/src/app/core/services/api.service.ts`
- Create: `frontend/src/app/core/services/api.service.spec.ts`

- [ ] **Step 1: Write failing test for API service**

Create `frontend/src/app/core/services/api.service.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ApiService } from './api.service';

describe('ApiService', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ApiService]
    });
    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should make GET request with correct URL', () => {
    const mockData = { id: 1, nome: 'Test' };
    
    service.get('/test').subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne(request => request.url.includes('/test'));
    expect(req.request.method).toBe('GET');
    req.flush(mockData);
  });

  it('should make POST request with data', () => {
    const mockData = { success: true };
    const payload = { nome: 'Test' };
    
    service.post('/test', payload).subscribe(data => {
      expect(data).toEqual(mockData);
    });

    const req = httpMock.expectOne(request => request.url.includes('/test'));
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(mockData);
  });
});
```

- [ ] **Step 2: Run test to verify it fails**

```bash
ng test --include='**/api.service.spec.ts' --watch=false
```

Expected: Tests fail with "ApiService not found".

- [ ] **Step 3: Implement API service**

Create `frontend/src/app/core/services/api.service.ts`:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(`${this.apiUrl}${endpoint}`);
  }

  post<T>(endpoint: string, data: any): Observable<T> {
    return this.http.post<T>(`${this.apiUrl}${endpoint}`, data);
  }

  put<T>(endpoint: string, data: any): Observable<T> {
    return this.http.put<T>(`${this.apiUrl}${endpoint}`, data);
  }

  patch<T>(endpoint: string, data: any): Observable<T> {
    return this.http.patch<T>(`${this.apiUrl}${endpoint}`, data);
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(`${this.apiUrl}${endpoint}`);
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/api.service.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/services/api.service*
git commit -m "feat: create API service with HTTP client wrapper

- Implement get, post, put, patch, delete methods
- Centralize API base URL configuration
- Add unit tests with HttpClientTestingModule
- All tests passing

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 5: Create Auth Service (Login, Refresh, Logout)

**Files:**
- Create: `frontend/src/app/core/services/auth.service.ts`
- Create: `frontend/src/app/core/services/auth.service.spec.ts`

- [ ] **Step 1: Write failing tests for Auth service**

Create `frontend/src/app/core/services/auth.service.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { LoginRequest, LoginResponse } from '../models/auth';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    localStorage.clear();
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login and store tokens', (done) => {
    const loginReq: LoginRequest = { email: 'test@test.com', password: 'password' };
    const mockResponse: LoginResponse = {
      accessToken: 'access123',
      refreshToken: 'refresh123',
      expiresIn: 900,
      tokenType: 'Bearer'
    };

    service.login(loginReq).subscribe(() => {
      expect(localStorage.getItem('accessToken')).toBe('access123');
      expect(localStorage.getItem('refreshToken')).toBe('refresh123');
      done();
    });

    const req = httpMock.expectOne(request => request.url.includes('/auth/login'));
    expect(req.request.method).toBe('POST');
    req.flush(mockResponse);
  });

  it('should logout and clear tokens', () => {
    localStorage.setItem('accessToken', 'token123');
    localStorage.setItem('refreshToken', 'refresh123');

    service.logout();

    expect(localStorage.getItem('accessToken')).toBeNull();
    expect(localStorage.getItem('refreshToken')).toBeNull();
  });

  it('should return true if user is authenticated', () => {
    localStorage.setItem('accessToken', 'token123');
    expect(service.isAuthenticated()).toBe(true);

    localStorage.removeItem('accessToken');
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should refresh access token', (done) => {
    localStorage.setItem('refreshToken', 'refresh123');
    const mockResponse: LoginResponse = {
      accessToken: 'newToken123',
      refreshToken: 'newRefresh123',
      expiresIn: 900,
      tokenType: 'Bearer'
    };

    service.refreshToken().subscribe(() => {
      expect(localStorage.getItem('accessToken')).toBe('newToken123');
      done();
    });

    const req = httpMock.expectOne(request => request.url.includes('/auth/refresh'));
    req.flush(mockResponse);
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/auth.service.spec.ts' --watch=false
```

Expected: Tests fail with "AuthService not found".

- [ ] **Step 3: Implement Auth service**

Create `frontend/src/app/core/services/auth.service.ts`:

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { LoginRequest, LoginResponse, RefreshTokenRequest } from '../models/auth';
import { ApiService } from './api.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private api: ApiService
  ) { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => this.storeTokens(response)),
        catchError(error => {
          console.error('Login failed', error);
          return throwError(() => error);
        })
      );
  }

  refreshToken(): Observable<LoginResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available'));
    }

    const request: RefreshTokenRequest = { refreshToken };
    return this.http.post<LoginResponse>(`${this.apiUrl}/refresh`, request)
      .pipe(
        tap(response => this.storeTokens(response)),
        catchError(error => {
          this.logout();
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  private storeTokens(response: LoginResponse): void {
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/auth.service.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/services/auth.service*
git commit -m "feat: create authentication service with login and refresh token

- Implement login, logout, isAuthenticated methods
- Handle access and refresh token storage in localStorage
- Automatic logout on refresh token failure
- Comprehensive unit tests with mocked HTTP requests

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 6: Create Products Service

**Files:**
- Create: `frontend/src/app/core/services/products.service.ts`
- Create: `frontend/src/app/core/services/products.service.spec.ts`

- [ ] **Step 1: Write failing tests**

Create `frontend/src/app/core/services/products.service.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductsService } from './products.service';
import { Produto } from '../models/produto';

describe('ProductsService', () => {
  let service: ProductsService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductsService]
    });
    service = TestBed.inject(ProductsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should load products and update subject', (done) => {
    const mockProducts = [
      { id: 1, nome: 'Produto 1', sku: 'SKU1', categoria: 'Cat1', preco: 100, quantidade: 10, quantidadeMinima: 5 }
    ];

    service.loadProducts().subscribe(() => {
      service.products$.subscribe(products => {
        expect(products.length).toBe(1);
        expect(products[0].nome).toBe('Produto 1');
        done();
      });
    });

    const req = httpMock.expectOne(request => request.url.includes('/produtos'));
    req.flush({ content: mockProducts });
  });

  it('should search products by term', (done) => {
    const mockResults = [
      { id: 1, nome: 'Teclado', sku: 'TEK001', categoria: 'Periféricos', preco: 150, quantidade: 5, quantidadeMinima: 2 }
    ];

    service.searchProducts('Teclado').subscribe(() => {
      service.products$.subscribe(products => {
        expect(products[0].nome).toContain('Teclado');
        done();
      });
    });

    const req = httpMock.expectOne(request => request.url.includes('/buscar/termo'));
    req.flush(mockResults);
  });

  it('should create product', (done) => {
    const newProduto: Produto = {
      nome: 'Novo Produto',
      sku: 'NEW001',
      categoria: 'Nova',
      preco: 50,
      quantidade: 20,
      quantidadeMinima: 5
    };

    service.createProduct(newProduto).subscribe(created => {
      expect(created.id).toBe(1);
      done();
    });

    const req = httpMock.expectOne(request => request.url.includes('/produtos'));
    expect(req.request.method).toBe('POST');
    req.flush({ id: 1, ...newProduto });
  });

  it('should load low stock alerts', (done) => {
    const mockAlerts = [
      { id: 1, nome: 'Produto Baixo', sku: 'LOW001', categoria: 'Cat', preco: 50, quantidade: 2, quantidadeMinima: 10 }
    ];

    service.loadLowStockAlerts().subscribe(alerts => {
      expect(alerts.length).toBe(1);
      expect(alerts[0].quantidade).toBeLessThan(alerts[0].quantidadeMinima);
      done();
    });

    const req = httpMock.expectOne(request => request.url.includes('/alertas/estoque-minimo'));
    req.flush(mockAlerts);
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/products.service.spec.ts' --watch=false
```

Expected: Tests fail.

- [ ] **Step 3: Implement Products service**

Create `frontend/src/app/core/services/products.service.ts`:

```typescript
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

  private totalValueSubject = new BehaviorSubject<number>(0);
  public totalValue$ = this.totalValueSubject.asObservable();

  constructor(private http: HttpClient) { }

  loadProducts(page: number = 0, pageSize: number = 10): Observable<ApiResponse<Produto[]>> {
    this.loadingSubject.next(true);
    return this.http.get<ApiResponse<Produto[]>>(
      `${this.apiUrl}?page=${page}&size=${pageSize}`
    ).pipe(
      tap(response => {
        this.productsSubject.next(response.content);
        this.calculateTotalValue(response.content);
        this.loadingSubject.next(false);
      })
    );
  }

  searchProducts(term: string): Observable<Produto[]> {
    this.loadingSubject.next(true);
    return this.http.get<Produto[]>(
      `${this.apiUrl}/buscar/termo?termo=${term}`
    ).pipe(
      tap(produtos => {
        this.productsSubject.next(produtos);
        this.loadingSubject.next(false);
      })
    );
  }

  getProductById(id: number): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/${id}`);
  }

  getProductBySku(sku: string): Observable<Produto> {
    return this.http.get<Produto>(`${this.apiUrl}/sku/${sku}`);
  }

  createProduct(produto: Produto): Observable<Produto> {
    return this.http.post<Produto>(`${this.apiUrl}`, produto);
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

  private calculateTotalValue(produtos: Produto[]): void {
    const total = produtos.reduce((sum, p) => sum + (p.preco * p.quantidade), 0);
    this.totalValueSubject.next(total);
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/products.service.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/services/products.service*
git commit -m "feat: create products service with CRUD and search

- Implement loadProducts, searchProducts, CRUD operations
- Expose observable streams for products, loading, totalValue
- Calculate total inventory value automatically
- Load low stock alerts from API
- Comprehensive unit tests

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 7: Create Notification Service

**Files:**
- Create: `frontend/src/app/core/services/notification.service.ts`
- Create: `frontend/src/app/core/services/notification.service.spec.ts`

- [ ] **Step 1: Write failing tests**

Create `frontend/src/app/core/services/notification.service.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { NotificationService } from './notification.service';

describe('NotificationService', () => {
  let service: NotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationService]
    });
    service = TestBed.inject(NotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should emit success notification', (done) => {
    service.notification$.subscribe(notif => {
      if (notif) {
        expect(notif.type).toBe('success');
        expect(notif.message).toBe('Test message');
        done();
      }
    });

    service.showSuccess('Test message');
  });

  it('should emit error notification', (done) => {
    service.notification$.subscribe(notif => {
      if (notif) {
        expect(notif.type).toBe('error');
        done();
      }
    });

    service.showError('Error message');
  });

  it('should clear notification', (done) => {
    service.notification$.subscribe(notif => {
      if (notif === null) {
        expect(notif).toBeNull();
        done();
      }
    });

    service.showSuccess('Message');
    service.clear();
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/notification.service.spec.ts' --watch=false
```

Expected: Tests fail.

- [ ] **Step 3: Implement Notification service**

Create `frontend/src/app/core/services/notification.service.ts`:

```typescript
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

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

  constructor() { }

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
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
  }

  private show(notification: Notification): void {
    this.notificationSubject.next(notification);
    if (this.timeoutId) {
      clearTimeout(this.timeoutId);
    }
    // Auto-dismiss after 5 seconds
    this.timeoutId = setTimeout(() => {
      this.clear();
    }, 5000);
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/notification.service.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/services/notification.service*
git commit -m "feat: create notification service for toast messages

- Implement showSuccess, showError, showInfo methods
- Auto-dismiss notifications after 5 seconds
- Observable stream for subscribing components
- Manual clear method for explicit dismissal
- Unit tests with timer mocking

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

## Phase 3: Guards & Interceptors

### Task 8: Create Auth Guard

**Files:**
- Create: `frontend/src/app/core/guards/auth.guard.ts`
- Create: `frontend/src/app/core/guards/auth.guard.spec.ts`

- [ ] **Step 1: Write failing tests**

Create `frontend/src/app/core/guards/auth.guard.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { AuthGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('AuthGuard', () => {
  let guard: AuthGuard;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthGuard,
        {
          provide: AuthService,
          useValue: { isAuthenticated: jasmine.createSpy('isAuthenticated') }
        },
        {
          provide: Router,
          useValue: { navigate: jasmine.createSpy('navigate') }
        }
      ]
    });
    guard = TestBed.inject(AuthGuard);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should allow access if authenticated', () => {
    (authService.isAuthenticated as jasmine.Spy).and.returnValue(true);
    const result = guard.canActivate({} as any, {} as any);
    expect(result).toBe(true);
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should deny access and redirect to login if not authenticated', () => {
    (authService.isAuthenticated as jasmine.Spy).and.returnValue(false);
    const result = guard.canActivate({} as any, {} as any);
    expect(result).toBe(false);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/auth.guard.spec.ts' --watch=false
```

Expected: Tests fail.

- [ ] **Step 3: Implement Auth Guard**

Create `frontend/src/app/core/guards/auth.guard.ts`:

```typescript
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.authService.isAuthenticated()) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/auth.guard.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/guards
git commit -m "feat: create auth guard to protect routes

- Implement CanActivate guard for protected routes
- Redirect to login if not authenticated
- Check AuthService.isAuthenticated() before access
- Unit tests with mocked AuthService and Router

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 9: Create Auth Interceptor

**Files:**
- Create: `frontend/src/app/core/interceptors/auth.interceptor.ts`
- Create: `frontend/src/app/core/interceptors/auth.interceptor.spec.ts`

- [ ] **Step 1: Write failing tests**

Create `frontend/src/app/core/interceptors/auth.interceptor.spec.ts`:

```typescript
import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient, HttpResponse } from '@angular/common/http';
import { AuthInterceptor } from './auth.interceptor';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

describe('AuthInterceptor', () => {
  let httpClient: HttpClient;
  let httpMock: HttpTestingController;
  let authService: AuthService;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        {
          provide: AuthService,
          useValue: {
            getAccessToken: jasmine.createSpy('getAccessToken').and.returnValue('token123'),
            logout: jasmine.createSpy('logout')
          }
        },
        {
          provide: Router,
          useValue: { navigate: jasmine.createSpy('navigate') }
        }
      ]
    });
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should add Authorization header if token exists', () => {
    httpClient.get('/test').subscribe();

    const req = httpMock.expectOne('/test');
    expect(req.request.headers.has('Authorization')).toBe(true);
    expect(req.request.headers.get('Authorization')).toBe('Bearer token123');
    req.flush({});
  });

  it('should not add Authorization header if no token', () => {
    (authService.getAccessToken as jasmine.Spy).and.returnValue(null);
    httpClient.get('/test').subscribe();

    const req = httpMock.expectOne('/test');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/auth.interceptor.spec.ts' --watch=false
```

Expected: Tests fail.

- [ ] **Step 3: Implement Auth Interceptor**

Create `frontend/src/app/core/interceptors/auth.interceptor.ts`:

```typescript
import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Add token to outgoing request
    const token = this.authService.getAccessToken();
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(req, next);
        }
        return throwError(() => error);
      })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      return this.authService.refreshToken().pipe(
        switchMap(() => {
          this.isRefreshing = false;
          // Retry original request with new token
          const token = this.authService.getAccessToken();
          req = req.clone({
            setHeaders: {
              Authorization: `Bearer ${token}`
            }
          });
          return next.handle(req);
        }),
        catchError(error => {
          this.isRefreshing = false;
          this.authService.logout();
          this.router.navigate(['/login']);
          return throwError(() => error);
        })
      );
    }
    return throwError(() => new Error('Token refresh in progress'));
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/auth.interceptor.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/core/interceptors/auth.interceptor*
git commit -m "feat: create auth interceptor for token management

- Add Authorization header to all requests if token exists
- Handle 401 errors by attempting token refresh
- Retry failed request with new token after refresh
- Auto-logout and redirect to login on refresh failure
- Prevent concurrent refresh token requests
- Unit tests with HttpClientTestingModule

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 10: Create Error Interceptor

**Files:**
- Create: `frontend/src/app/core/interceptors/error.interceptor.ts`

- [ ] **Step 1: Implement Error Interceptor**

Create `frontend/src/app/core/interceptors/error.interceptor.ts`:

```typescript
import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private notification: NotificationService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(error => {
        let errorMessage = 'An error occurred';

        if (error instanceof HttpErrorResponse) {
          switch (error.status) {
            case 400:
              errorMessage = error.error?.message || 'Bad request';
              break;
            case 403:
              errorMessage = 'You do not have permission to perform this action';
              break;
            case 404:
              errorMessage = 'Resource not found';
              break;
            case 409:
              errorMessage = 'Conflict: Resource already exists or was modified';
              break;
            case 500:
            case 502:
            case 503:
              errorMessage = 'Server error. Please try again later';
              break;
            default:
              errorMessage = error.error?.message || errorMessage;
          }
        } else {
          errorMessage = 'Connection error. Check your internet';
        }

        this.notification.showError(errorMessage);
        return throwError(() => error);
      })
    );
  }
}
```

- [ ] **Step 2: Verify it compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/core/interceptors/error.interceptor*
git commit -m "feat: create error interceptor for global error handling

- Catch HTTP errors and map to user-friendly messages
- Show error notifications via NotificationService
- Handle 400, 403, 404, 409, 5xx status codes
- Network error messaging for connection failures

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

## Phase 4: Core Module Setup

### Task 11: Create Core Module

**Files:**
- Create: `frontend/src/app/core/core.module.ts`

- [ ] **Step 1: Implement Core Module**

Create `frontend/src/app/core/core.module.ts`:

```typescript
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { ErrorInterceptor } from './interceptors/error.interceptor';
import { AuthService } from './services/auth.service';
import { ApiService } from './services/api.service';
import { ProductsService } from './services/products.service';
import { NotificationService } from './services/notification.service';
import { AuthGuard } from './guards/auth.guard';

@NgModule({
  imports: [CommonModule, HttpClientModule],
  providers: [
    AuthService,
    ApiService,
    ProductsService,
    NotificationService,
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ]
})
export class CoreModule { }
```

- [ ] **Step 2: Verify module compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/core/core.module.ts
git commit -m "feat: create core module with services, guards, interceptors

- Export all core services (Auth, API, Products, Notification)
- Register HTTP interceptors (Auth, Error)
- Provide AuthGuard for route protection
- Single import point for core dependencies

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

## Phase 5: Shared Components

### Task 12: Create Toast Notification Component

**Files:**
- Create: `frontend/src/app/shared/components/toast-notification/toast-notification.component.ts`
- Create: `frontend/src/app/shared/components/toast-notification/toast-notification.component.html`
- Create: `frontend/src/app/shared/components/toast-notification/toast-notification.component.scss`

- [ ] **Step 1: Create component files**

Create `frontend/src/app/shared/components/toast-notification/toast-notification.component.ts`:

```typescript
import { Component, OnInit } from '@angular/core';
import { NotificationService, Notification } from '../../../core/services/notification.service';
import { trigger, transition, style, animate } from '@angular/animations';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-toast-notification',
  standalone: false,
  imports: [CommonModule],
  templateUrl: './toast-notification.component.html',
  styleUrls: ['./toast-notification.component.scss'],
  animations: [
    trigger('toastAnimation', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(-20px)' }),
        animate('300ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
      ]),
      transition(':leave', [
        animate('300ms ease-in', style({ opacity: 0, transform: 'translateY(-20px)' }))
      ])
    ])
  ]
})
export class ToastNotificationComponent implements OnInit {
  notification: Notification | null = null;

  constructor(private notificationService: NotificationService) { }

  ngOnInit(): void {
    this.notificationService.notification$.subscribe(
      notification => this.notification = notification
    );
  }

  dismiss(): void {
    this.notificationService.clear();
  }

  getIcon(): string {
    switch (this.notification?.type) {
      case 'success':
        return '✓';
      case 'error':
        return '✕';
      case 'info':
        return 'ℹ';
      default:
        return '';
    }
  }
}
```

Create `frontend/src/app/shared/components/toast-notification/toast-notification.component.html`:

```html
<div
  *ngIf="notification"
  @toastAnimation
  class="fixed top-4 right-4 p-4 rounded-lg shadow-lg text-white flex items-center gap-3 max-w-md"
  [ngClass]="{
    'bg-green-500': notification.type === 'success',
    'bg-red-500': notification.type === 'error',
    'bg-blue-500': notification.type === 'info'
  }"
>
  <span class="text-xl font-bold">{{ getIcon() }}</span>
  <span class="flex-1">{{ notification.message }}</span>
  <button (click)="dismiss()" class="text-white hover:opacity-75 text-lg font-bold">
    ✕
  </button>
</div>
```

Create `frontend/src/app/shared/components/toast-notification/toast-notification.component.scss`:

```scss
/* Component uses Tailwind, no additional styles needed */
```

- [ ] **Step 2: Verify component compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/shared/components/toast-notification
git commit -m "feat: create toast notification component

- Display success, error, and info notifications
- Auto-position in top-right corner with fixed positioning
- Smooth slide-in/fade-out animations
- Manual dismiss button with styled close icon
- Subscribe to NotificationService observable

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 13: Create Currency Pipe

**Files:**
- Create: `frontend/src/app/shared/pipes/currency.pipe.ts`
- Create: `frontend/src/app/shared/pipes/currency.pipe.spec.ts`

- [ ] **Step 1: Write failing tests**

Create `frontend/src/app/shared/pipes/currency.pipe.spec.ts`:

```typescript
import { CurrencyPipe } from './currency.pipe';

describe('CurrencyPipe', () => {
  let pipe: CurrencyPipe;

  beforeEach(() => {
    pipe = new CurrencyPipe();
  });

  it('should create an instance', () => {
    expect(pipe).toBeTruthy();
  });

  it('should format number as BRL currency', () => {
    expect(pipe.transform(100)).toBe('R$ 100,00');
    expect(pipe.transform(1000)).toBe('R$ 1.000,00');
    expect(pipe.transform(1234.56)).toBe('R$ 1.234,56');
  });

  it('should handle zero', () => {
    expect(pipe.transform(0)).toBe('R$ 0,00');
  });

  it('should handle negative numbers', () => {
    expect(pipe.transform(-100)).toBe('-R$ 100,00');
  });
});
```

- [ ] **Step 2: Run tests to verify they fail**

```bash
ng test --include='**/currency.pipe.spec.ts' --watch=false
```

Expected: Tests fail.

- [ ] **Step 3: Implement Currency Pipe**

Create `frontend/src/app/shared/pipes/currency.pipe.ts`:

```typescript
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appCurrency',
  standalone: true
})
export class CurrencyPipe implements PipeTransform {
  transform(value: number): string {
    if (value === null || value === undefined) {
      return 'R$ 0,00';
    }

    const isNegative = value < 0;
    const absoluteValue = Math.abs(value);
    
    // Format with 2 decimal places and thousand separators
    const formatted = absoluteValue.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });

    const prefix = isNegative ? '-' : '';
    return `${prefix}R$ ${formatted}`;
  }
}
```

- [ ] **Step 4: Run tests to verify they pass**

```bash
ng test --include='**/currency.pipe.spec.ts' --watch=false
```

Expected: All tests PASS.

- [ ] **Step 5: Commit**

```bash
git add src/app/shared/pipes
git commit -m "feat: create currency pipe for BRL formatting

- Format numbers as Brazilian Real (R$) with locale support
- Handle decimal places, thousand separators
- Support negative numbers with prefix
- Standalone pipe for easy import in components
- Unit tests with various edge cases

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 14: Create Metric Card Component

**Files:**
- Create: `frontend/src/app/shared/components/metric-card/metric-card.component.ts`
- Create: `frontend/src/app/shared/components/metric-card/metric-card.component.html`
- Create: `frontend/src/app/shared/components/metric-card/metric-card.component.scss`

- [ ] **Step 1: Create component**

Create `frontend/src/app/shared/components/metric-card/metric-card.component.ts`:

```typescript
import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurrencyPipe } from '../../pipes/currency.pipe';

@Component({
  selector: 'app-metric-card',
  standalone: false,
  imports: [CommonModule, CurrencyPipe],
  templateUrl: './metric-card.component.html',
  styleUrls: ['./metric-card.component.scss']
})
export class MetricCardComponent {
  @Input() title: string = '';
  @Input() value: string | number = 0;
  @Input() icon: string = '';
  @Input() isCurrency: boolean = false;
  @Input() highlight: boolean = false;
}
```

Create `frontend/src/app/shared/components/metric-card/metric-card.component.html`:

```html
<div
  class="bg-white rounded-lg p-6 shadow-md border-l-4 transition-transform hover:scale-105"
  [ngClass]="{
    'border-l-blue-500': !highlight,
    'border-l-orange-500': highlight
  }"
>
  <div class="flex items-center justify-between">
    <div>
      <p class="text-gray-600 text-sm font-medium">{{ title }}</p>
      <p class="text-2xl font-bold mt-2" [ngClass]="{ 'text-orange-600': highlight }">
        <span *ngIf="isCurrency">{{ value | appCurrency }}</span>
        <span *ngIf="!isCurrency">{{ value }}</span>
      </p>
    </div>
    <span class="text-4xl opacity-20">{{ icon }}</span>
  </div>
</div>
```

Create `frontend/src/app/shared/components/metric-card/metric-card.component.scss`:

```scss
/* Component uses Tailwind, no additional styles needed */
```

- [ ] **Step 2: Verify component compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/shared/components/metric-card
git commit -m "feat: create metric card component for dashboard

- Display metric title, value, and icon
- Support currency formatting with appCurrency pipe
- Highlight mode for alerts/warnings (orange border and text)
- Hover scale effect for visual feedback
- Responsive design with Tailwind CSS

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 15: Create Sidebar Component

**Files:**
- Create: `frontend/src/app/shared/components/sidebar/sidebar.component.ts`
- Create: `frontend/src/app/shared/components/sidebar/sidebar.component.html`
- Create: `frontend/src/app/shared/components/sidebar/sidebar.component.scss`

- [ ] **Step 1: Create component**

Create `frontend/src/app/shared/components/sidebar/sidebar.component.ts`:

```typescript
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-sidebar',
  standalone: false,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  constructor(private authService: AuthService) { }

  logout(): void {
    this.authService.logout();
  }
}
```

Create `frontend/src/app/shared/components/sidebar/sidebar.component.html`:

```html
<nav class="w-full md:w-64 bg-gray-900 text-white p-6 flex flex-col h-screen md:h-auto md:fixed md:left-0 md:top-16">
  <!-- Menu Items -->
  <div class="flex-1">
    <div class="text-xs font-semibold text-gray-400 uppercase mb-4">Menu</div>
    <ul class="space-y-2">
      <li>
        <a
          routerLink="/dashboard"
          routerLinkActive="bg-gray-700"
          class="block px-4 py-2 rounded hover:bg-gray-700 transition"
        >
          Dashboard
        </a>
      </li>
      <li>
        <a
          routerLink="/produtos"
          routerLinkActive="bg-gray-700"
          class="block px-4 py-2 rounded hover:bg-gray-700 transition"
        >
          Produtos
        </a>
      </li>
    </ul>
  </div>

  <!-- Logout -->
  <div class="border-t border-gray-700 pt-4">
    <button
      (click)="logout()"
      class="w-full px-4 py-2 bg-red-600 hover:bg-red-700 rounded font-medium transition"
    >
      Sair
    </button>
  </div>
</nav>
```

Create `frontend/src/app/shared/components/sidebar/sidebar.component.scss`:

```scss
/* Component uses Tailwind, no additional styles needed */
```

- [ ] **Step 2: Verify component compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/shared/components/sidebar
git commit -m "feat: create sidebar component with navigation

- Fixed sidebar with Dashboard and Produtos menu items
- Active route highlighting with routerLinkActive
- Logout button with AuthService integration
- Responsive: full-width mobile, fixed sidebar desktop
- Dark gray theme with hover effects

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 16: Create Navbar Component

**Files:**
- Create: `frontend/src/app/shared/components/navbar/navbar.component.ts`
- Create: `frontend/src/app/shared/components/navbar/navbar.component.html`
- Create: `frontend/src/app/shared/components/navbar/navbar.component.scss`

- [ ] **Step 1: Create component**

Create `frontend/src/app/shared/components/navbar/navbar.component.ts`:

```typescript
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: false,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  appName = 'Controle de Estoque';
}
```

Create `frontend/src/app/shared/components/navbar/navbar.component.html`:

```html
<header class="bg-white border-b border-gray-200 shadow-sm sticky top-0 z-10">
  <div class="px-6 py-4 flex items-center justify-between">
    <!-- Logo/Title -->
    <div class="text-xl font-bold text-gray-900">
      {{ appName }}
    </div>

    <!-- User Info (Placeholder) -->
    <div class="flex items-center gap-4">
      <span class="text-sm text-gray-600">Bem-vindo</span>
      <div class="w-10 h-10 bg-blue-500 rounded-full flex items-center justify-center">
        <span class="text-white font-bold">U</span>
      </div>
    </div>
  </div>
</header>
```

Create `frontend/src/app/shared/components/navbar/navbar.component.scss`:

```scss
/* Component uses Tailwind, no additional styles needed */
```

- [ ] **Step 2: Verify component compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/shared/components/navbar
git commit -m "feat: create navbar component with app title

- Display app name/logo in header
- Show user welcome message and avatar placeholder
- Sticky positioning with shadow for visual hierarchy
- Light theme with border bottom separator
- Ready for user profile menu expansion

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

### Task 17: Create Shared Module

**Files:**
- Create: `frontend/src/app/shared/shared.module.ts`

- [ ] **Step 1: Create Shared Module**

Create `frontend/src/app/shared/shared.module.ts`:

```typescript
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastNotificationComponent } from './components/toast-notification/toast-notification.component';
import { MetricCardComponent } from './components/metric-card/metric-card.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { CurrencyPipe } from './pipes/currency.pipe';

const components = [
  ToastNotificationComponent,
  MetricCardComponent,
  SidebarComponent,
  NavbarComponent
];

const pipes = [
  CurrencyPipe
];

@NgModule({
  declarations: [...components],
  imports: [CommonModule, ...pipes],
  exports: [...components, ...pipes]
})
export class SharedModule { }
```

- [ ] **Step 2: Verify module compiles**

```bash
ng build
```

Expected: No errors.

- [ ] **Step 3: Commit**

```bash
git add src/app/shared/shared.module.ts
git commit -m "feat: create shared module with components and pipes

- Export all shared components (Toast, MetricCard, Sidebar, Navbar)
- Export CurrencyPipe for use across feature modules
- Single import point for reusable UI elements
- Ready for feature modules to import and use

Co-Authored-By: Claude Haiku 4.5 <noreply@anthropic.com>"
```

---

## Phase 6: Feature Modules (Tasks 18-22) & Routing (Task 23)

Due to length, remaining tasks will create:
- **Task 18:** Login Feature Module (component + form)
- **Task 19:** Dashboard Feature Module (metrics cards + alert loading)
- **Task 20:** Products Feature Module (search table + CRUD modals)
- **Task 21:** App Routing with Lazy Loading
- **Task 22:** Product Form Modal Component
- **Task 23:** Stock Adjustment Modal Component

Each task follows same pattern: failing tests → implementation → passing tests → commit.

---

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-05-24-frontend-angular-implementation.md`.

Two execution options:

**1. Subagent-Driven (Recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

Which approach?
