# Frontend Angular SPA - Design Specification
**Date:** 2026-05-24  
**Status:** Design Phase  
**Target Stack:** Angular (latest), Tailwind CSS, RxJS Services, Lazy Loading

---

## 1. Overview

Build a minimalista, clean, light-theme Angular Single Page Application (SPA) for inventory management. Target micro/small business owners with intuitive, fast, mobile-first interface. All interaction patterns designed for rapid data entry and quick decision-making.

**Core Principle:** Form follows function. No decorative elements. Every UI element serves a purpose.

---

## 2. Technology Stack

| Layer | Technology | Rationale |
|-------|-----------|-----------|
| Framework | Angular 18+ | Enterprise-grade, great for scalable SPAs |
| Styling | Tailwind CSS + Custom | Light theme, mobile-first, minimal configuration |
| State | RxJS Services + Observables | Lightweight, no extra framework overhead |
| Routing | Angular Router + Lazy Loading | Feature modules load on-demand |
| Forms | Reactive Forms | Type-safe, better validation, easier testing |
| HTTP | Angular HttpClient + Interceptors | Built-in, clean token management |
| Build | Angular CLI | Standard tooling, no custom setup |
| Auth Storage | localStorage + Refresh Token | localStorage persists across sessions, refresh token for expiry |

---

## 3. Folder Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                          # Global services, guards, interceptors
│   │   │   ├── services/
│   │   │   │   ├── auth.service.ts        # Login, token refresh, logout
│   │   │   │   ├── api.service.ts         # HTTP client wrapper
│   │   │   │   ├── products.service.ts    # Product CRUD + search
│   │   │   │   └── notification.service.ts # Toast/alert notifications
│   │   │   ├── guards/
│   │   │   │   └── auth.guard.ts          # Route protection
│   │   │   ├── interceptors/
│   │   │   │   └── auth.interceptor.ts    # Add token to headers, handle 401
│   │   │   ├── models/
│   │   │   │   ├── produto.ts
│   │   │   │   ├── auth.ts
│   │   │   │   └── api-response.ts
│   │   │   └── core.module.ts
│   │   │
│   │   ├── shared/                        # Reusable components, utilities
│   │   │   ├── components/
│   │   │   │   ├── navbar/
│   │   │   │   │   ├── navbar.component.ts
│   │   │   │   │   ├── navbar.component.html
│   │   │   │   │   └── navbar.component.scss
│   │   │   │   ├── sidebar/
│   │   │   │   │   ├── sidebar.component.ts
│   │   │   │   │   ├── sidebar.component.html
│   │   │   │   │   └── sidebar.component.scss
│   │   │   │   ├── metric-card/           # Dashboard metric display
│   │   │   │   ├── product-table/         # Reusable table component
│   │   │   │   ├── modal-form/            # Generic product form modal
│   │   │   │   ├── modal-stock-adj/       # Stock adjustment modal
│   │   │   │   ├── loading-spinner/       # Loading indicator
│   │   │   │   └── toast-notification/    # Toast messages
│   │   │   ├── pipes/
│   │   │   │   └── currency.pipe.ts       # BRL formatting
│   │   │   └── shared.module.ts
│   │   │
│   │   ├── features/                      # Feature modules (lazy loaded)
│   │   │   ├── login/
│   │   │   │   ├── login.component.ts
│   │   │   │   ├── login.component.html
│   │   │   │   ├── login.component.scss
│   │   │   │   └── login.module.ts
│   │   │   ├── dashboard/
│   │   │   │   ├── dashboard.component.ts
│   │   │   │   ├── dashboard.component.html
│   │   │   │   ├── dashboard.component.scss
│   │   │   │   └── dashboard.module.ts
│   │   │   └── products/
│   │   │       ├── products.component.ts  # Smart container
│   │   │       ├── products.component.html
│   │   │       ├── products.component.scss
│   │   │       ├── products.service.ts    # Feature-specific logic
│   │   │       └── products.module.ts
│   │   │
│   │   ├── app-routing.module.ts
│   │   └── app.module.ts
│   │
│   ├── assets/                            # Images, logos
│   ├── styles/
│   │   ├── tailwind.css
│   │   ├── variables.scss                 # Color palette, spacing
│   │   └── global.scss
│   ├── index.html
│   └── main.ts
│
├── tailwind.config.js
├── angular.json
├── tsconfig.json
├── package.json
└── README.md
```

---

## 4. Authentication Flow

### 4.1 Login Flow

```
User inputs email + password
         ↓
AuthService.login(email, password) → POST /api/auth/login
         ↓
Backend returns { accessToken, refreshToken, expiresIn }
         ↓
Store tokens in localStorage
         ↓
Navigate to /dashboard
```

### 4.2 Token Management

**Access Token:**
- Short-lived (15-30 min)
- Sent in every request header: `Authorization: Bearer <token>`
- Added by `AuthInterceptor`

**Refresh Token:**
- Long-lived (7 days)
- Stored in localStorage
- If access token expires (401 response) → `AuthInterceptor` automatically calls `AuthService.refreshToken()`
- If refresh fails → clear localStorage, redirect to login

### 4.3 AuthInterceptor Logic

```typescript
// Pseudo-code
intercept(req, next) {
  // Add token to outgoing requests
  const token = localStorage.getItem('accessToken');
  if (token) {
    req = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
  }
  
  return next.handle(req).pipe(
    catchError(err => {
      if (err.status === 401) {
        // Try refresh
        return this.authService.refreshToken().pipe(
          switchMap(() => next.handle(req)), // Retry original request
          catchError(() => {
            // Refresh failed, logout
            this.authService.logout();
            this.router.navigate(['/login']);
          })
        );
      }
      return throwError(err);
    })
  );
}
```

### 4.4 AuthGuard

Protects routes like `/dashboard`, `/produtos`. If token missing/invalid, redirects to `/login`.

---

## 5. State Management (RxJS Observable Pattern)

No centralized store (ngRx). Instead, each service exposes observables.

### 5.1 Service Pattern

```typescript
// Example: products.service.ts
@Injectable({ providedIn: 'root' })
export class ProductsService {
  private productsSubject = new BehaviorSubject<Produto[]>([]);
  public products$ = this.productsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private api: ApiService) {}

  loadProducts(page = 0, pageSize = 10) {
    this.loadingSubject.next(true);
    this.api.get(`/produtos?page=${page}&size=${pageSize}`).subscribe(
      (response: any) => {
        this.productsSubject.next(response.content);
        this.loadingSubject.next(false);
      },
      (error) => {
        this.loadingSubject.next(false);
        // Error handled by global error handler
      }
    );
  }

  searchProducts(term: string) {
    this.loadingSubject.next(true);
    this.api.get(`/produtos/buscar/termo?termo=${term}`).subscribe(
      (response: any) => {
        this.productsSubject.next(response);
        this.loadingSubject.next(false);
      },
      (error) => this.loadingSubject.next(false)
    );
  }

  createProduct(produto: Produto) {
    return this.api.post('/produtos', produto).pipe(
      tap((newProduto: Produto) => {
        const current = this.productsSubject.value;
        this.productsSubject.next([newProduto, ...current]);
      })
    );
  }
}
```

### 5.2 Component Subscription Pattern

Components subscribe using `| async` pipe (auto-unsubscribe on destroy):

```html
<!-- products.component.html -->
<div *ngIf="loading$ | async" class="spinner">Loading...</div>
<app-product-table 
  [produtos]="products$ | async"
  (onEdit)="editProduct($event)"
  (onDelete)="deleteProduct($event)">
</app-product-table>
```

---

## 6. Component Architecture

### 6.1 Smart vs Dumb Components

**Smart Components** (Feature Containers):
- Manage state (call services, subscribe to observables)
- Handle user events, business logic
- Example: `products.component.ts` — loads data, calls delete, refresh

**Dumb Components** (Presentational):
- Receive data via `@Input()`
- Emit events via `@Output()`
- No service dependencies
- Example: `product-table.component.ts` — just display rows, emit edit/delete clicks

**Communication:**
```
ProductsComponent (Smart)
    ↓ @Input [produtos]
ProductTableComponent (Dumb)
    ↓ @Output (onEdit)
ProductsComponent (Smart)
    ↓ Call service.updateProduct()
```

### 6.2 Modal Management

Modals are separate smart components, triggered from parent:

```typescript
// products.component.ts
showFormModal = false;
selectedProduct: Produto | null = null;

editProduct(produto: Produto) {
  this.selectedProduct = produto;
  this.showFormModal = true;
}

onModalSave(produto: Produto) {
  this.productsService.updateProduct(producto).subscribe(() => {
    this.showFormModal = false;
    this.productsService.loadProducts(); // Refresh
  });
}

onModalCancel() {
  this.showFormModal = false;
  this.selectedProduct = null;
}
```

HTML template:
```html
<app-modal-form 
  *ngIf="showFormModal"
  [produto]="selectedProduct"
  (onSave)="onModalSave($event)"
  (onCancel)="onModalCancel()">
</app-modal-form>
```

---

## 7. Screen Specifications

### 7.1 Login Screen

**Layout:** Centered card, light background, clean typography

**Components:**
- Logo/Title (discrete, top-center)
- Email input field
- Password input field (with eye toggle to show/mask)
- "Forgot password?" link (styling: light gray, small)
- "Sign In" button (primary color, full-width, hover effect)
- Error message display (red, below button if login fails)

**Behavior:**
- Reactive form validation (email format, password required)
- Disable button while loading
- Show spinner on submit
- Clear form after successful login
- Redirect to `/dashboard` on success
- Show toast error if credentials invalid

**Tailwind Styling:**
- Background: `bg-gray-50`
- Card: `bg-white shadow-lg rounded-lg p-8 w-full max-w-md`
- Inputs: `border border-gray-300 rounded px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500`
- Button: `bg-blue-600 hover:bg-blue-700 text-white font-medium py-2 px-4 rounded w-full`

### 7.2 Dashboard Screen

**Layout:** Navbar (top) + Sidebar (left) + Main content (right)

**Main Content:**
- Heading: "Dashboard"
- Grid of 3 metric cards (1 col mobile, 3 cols desktop at 1024px+)

**Card 1: Total Inventory Value**
- Title: "Valor Total em Estoque"
- Large number: sum of (preço × quantidade) for all products
- Icon: 💰 (or simple SVG)
- Format: BRL (e.g., "R$ 15.234,50")

**Card 2: Total Products**
- Title: "Giro de Produtos"
- Large number: total product count
- Icon: 📦
- Subtitle: "itens em estoque"

**Card 3: Low Stock Alerts**
- Title: "Alertas Críticos"
- Large number: count of products with quantity < minimum
- Icon: ⚠️ (or warning SVG)
- Color: orange/yellow warning tone if > 0
- Link: "Ver produtos" (goes to /products filtered by low stock)

**Sidebar:**
- Logo/App name (top, discrete)
- Menu items: Dashboard (active indicator), Produtos, Sair
- User info (optional, at bottom)

**Navbar:**
- App name (left)
- User avatar + "Hi, [Name]" (right)
- Logout button (icon or text)

### 7.3 Products Screen (Main Work Area)

**Layout:** Navbar + Sidebar + Main content

**Toolbar (top):**
- Search input (left): "Buscar por nome ou SKU..." → debounced, triggers `searchProducts()`
- "Novo Produto" button (right, primary color): opens modal form

**Product Table:**
- Columns: SKU | Nome | Categoria | Preço (R$) | Quantidade | Ações
- Striped rows (subtle zebra pattern: `nth-child(odd):bg-gray-50`)
- Row height: comfortable spacing
- Quantity column: text-orange if < estoque mínimo
- Ações column: Edit icon (pencil) | Delete icon (trash)
  - Edit: opens modal with pre-filled form
  - Delete: shows confirmation modal, then calls DELETE /api/produtos/{id}

**Pagination (bottom):**
- Current page indicator: "Página 1 de 5"
- Previous/Next buttons (disabled if at boundary)
- Optional: page size selector (10, 25, 50 items per page)

**Empty State:**
- If no products: centered message "Nenhum produto encontrado. Crie seu primeiro clicando em 'Novo Produto'"

### 7.4 Product Form Modal

**Trigger:** "Novo Produto" button OR Edit from table

**Form Structure:**
- Title: "Novo Produto" (create) or "Editar Produto" (edit)
- Close button (X) top-right

**Form Fields:**
1. **Nome** (required)
   - Input type: text
   - Validation: @NotBlank from backend
   - Error message: "Nome é obrigatório"

2. **SKU** (required)
   - Input type: text
   - Validation: unique (backend)
   - Error message: "SKU já existe"
   - Optional: Generate button (calls backend to generate unique SKU)

3. **Categoria** (optional)
   - Input type: text (can be dropdown if predefined categories)
   - Placeholder: "Ex: Eletrônicos"

4. **Preço** (required)
   - Input type: number with currency mask
   - Format: "R$ 0,00"
   - Validation: > 0

5. **Quantidade Mínima** (required)
   - Input type: number
   - Validation: >= 0

6. **Quantity Initial** (for creation only)
   - Input type: number
   - Only shown in create form

**Buttons:**
- "Cancelar" (secondary, gray): closes modal without saving
- "Salvar Produto" (primary, blue): POST/PUT then close and refresh table

**Validation:**
- Real-time error messages below each field
- Submit button disabled if form invalid
- Loading spinner on submit

### 7.5 Stock Adjustment Modal

**Trigger:** Clicking "Estoque Rápido" button on a product row (new column in table)

**Content:**
- Title: "Ajuste de Estoque"
- Product info: "Ajustando estoque de: [Nome]"
- Current stock display: "Estoque atual: 45"
- Input field: "Quantidade a adicionar/remover"
  - Positive = entrada (adding stock)
  - Negative = saída (removing stock)
  - Example user enters: 10 → adds 10 to current
  - Example user enters: -5 → removes 5 from current
- Preview: "Novo estoque será: 55"

**Buttons:**
- "Cancelar"
- "Confirmar Movimentação" (PATCH /api/produtos/{id}/estoque?quantidade=X)

**Behavior:**
- After success: close modal, show toast "Estoque atualizado", refresh products table
- After error: show error toast, keep modal open, user can retry

---

## 8. Error Handling & Notifications

### 8.1 Global Error Handler

Create `ErrorInterceptor` to catch all HTTP errors:

```typescript
// Catches errors from API calls
// Maps backend error messages to user-friendly strings
// Shows toast notification automatically
// Does NOT retry (that's AuthInterceptor's job for 401)
```

**Error Types:**
- `400 Bad Request`: Show validation errors from backend
- `401 Unauthorized`: Handle via AuthInterceptor (refresh or logout)
- `403 Forbidden`: "Você não tem permissão para esta ação"
- `404 Not Found`: "Recurso não encontrado"
- `409 Conflict`: "Conflito: recurso já existe ou foi modificado"
- `5xx Server Error`: "Erro no servidor. Tente novamente."
- Network error: "Erro de conexão. Verifique sua internet."

### 8.2 Toast Notifications

Simple toast system (no external lib):

```typescript
// notification.service.ts
showSuccess(message: string) { /* ... */ }
showError(message: string) { /* ... */ }
showInfo(message: string) { /* ... */ }
```

Toast displays for 5 seconds, auto-dismisses, can be manually closed.

### 8.3 Loading States

- Global spinner overlay for page-level loads (e.g., initial dashboard load)
- Inline spinners for component-level operations (e.g., table loading)
- Buttons show spinner + disabled state during async operations

---

## 9. Responsive Design

**Mobile-first approach:** Start with mobile layout, enhance for larger screens.

**Breakpoints:**
- `xs`: < 600px (phones)
- `sm`: 600px - 1023px (tablets)
- `lg`: 1024px+ (desktops)

**Grid Layouts:**
- Metric cards: 1 col (mobile) → 1 col (tablet) → 3 cols (desktop)
- Product table: scrollable horizontally on mobile, full-width on desktop
- Sidebar: hidden on mobile (hamburger menu), visible on tablet+
- Navbar: logo + hamburger (mobile), logo + user (desktop)

---

## 10. Performance Considerations

- **Lazy Loading:** Feature modules load on route navigation
- **OnPush Change Detection:** Use `ChangeDetectionStrategy.OnPush` on dumb components
- **Unsubscribe:** Use `| async` pipe or `takeUntil` with ngOnDestroy
- **Image Optimization:** Lazy load images, use appropriate formats
- **Caching:** Consider caching product list in service (cache invalidation on CRUD)
- **Debouncing:** Search input debounced (300ms) to reduce API calls

---

## 11. Testing Strategy

- **Unit Tests:** Services (API calls mocked), components (input/output testing)
- **Integration Tests:** Modal flow, search + pagination
- **E2E:** Login → Dashboard → Products → CRUD flow
- **Coverage Goal:** > 80%

---

## 12. Deployment

- **Build:** `ng build --configuration production`
- **Serve:** Static files (Angular output) served by backend or separate CDN
- **Environment:** `environment.prod.ts` with backend API URL
- **Docker:** Optional containerization for deployment consistency

---

## 13. Future Enhancements (Out of Scope)

- Real-time updates via WebSocket
- Dark mode toggle
- Multi-language (i18n)
- Batch import/export products
- Advanced reporting
- User roles & permissions

---

## 14. Success Criteria

✅ All CRUD operations work without page reload  
✅ Search returns results in < 1s  
✅ Mobile layout usable without horizontal scroll  
✅ Auth token refresh works transparently  
✅ Error messages guide user to correct action  
✅ No console errors or warnings  
✅ Lighthouse score > 85
