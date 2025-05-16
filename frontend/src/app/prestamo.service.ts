import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Prestamo } from './prestamo';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PrestamoService {
  private baseURL = 'http://localhost:8080/api';

  constructor(private httpClient: HttpClient) { }
 // Método para obtener la lista de idiomas
 obtenerPrestamosPorUsuario(usuarioId: number): Observable<Prestamo[]> {
    return this.httpClient.get<Prestamo[]>(`${this.baseURL}/prestamos/usuario/${usuarioId}`);
  }

  devolverPrestamo(idPrestamo: number): Observable<string> {
    return this.httpClient.post(`${this.baseURL}/prestamos/${idPrestamo}/return`, {}, { responseType: 'text' });
  }


}
