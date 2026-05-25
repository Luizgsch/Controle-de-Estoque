import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { NotificationService } from '../services/notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notification = inject(NotificationService);

  return next(req).pipe(
    catchError(error => {
      let message = 'Ocorreu um erro inesperado';

      if (error instanceof HttpErrorResponse) {
        switch (error.status) {
          case 400:
            message = error.error?.message || 'Requisição inválida';
            break;
          case 403:
            message = 'Você não tem permissão para esta ação';
            break;
          case 404:
            message = 'Recurso não encontrado';
            break;
          case 409:
            message = error.error?.message || 'Conflito: recurso já existe';
            break;
          case 500:
          case 502:
          case 503:
            message = 'Erro no servidor. Tente novamente em instantes';
            break;
          default:
            message = error.error?.message || message;
        }
      } else {
        message = 'Erro de conexão. Verifique sua internet';
      }

      notification.showError(message);
      return throwError(() => error);
    })
  );
};
