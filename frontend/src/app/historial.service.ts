import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Historial } from './historial';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HistorialService {
 private baseURL = 'http://localhost:8080/api';

  constructor(private httpClient: HttpClient) { }

   obtenerPrestamosPorUsuario(usuarioId: number): Observable<Historial[]> {
      return this.httpClient.get<Historial[]>(`${this.baseURL}/historial/usuario/${usuarioId}`);
    }
  
}
