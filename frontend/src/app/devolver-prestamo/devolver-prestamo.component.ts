import { Component } from '@angular/core';
import { PerfilDTO } from '../dto/perfil-dto';
import { PrestamoService } from '../prestamo.service';
import { Router } from '@angular/router';
import { PerfilService } from '../services/perfil.service';
import { Prestamo } from '../prestamo';

import { OnInit } from '@angular/core';

@Component({
  selector: 'app-devolver-prestamo',
  standalone: false,
  templateUrl: './devolver-prestamo.component.html',
  styleUrls: ['./devolver-prestamo.component.css']
})
export class DevolverPrestamoComponent implements OnInit {

  prestamos: Prestamo[] = [];
  perfilCompleto!: PerfilDTO;

  constructor(
    private prestamoServicio: PrestamoService,
    private route: Router,
    private usuarioService: PerfilService
  ) { }

  ngOnInit(): void {
    const id = localStorage.getItem('idusuario');

    if (id) {
      const usuarioId = Number(id);

      // Cargar perfil
      this.usuarioService.getPerfil(usuarioId).subscribe({
        next: (data) => {
          this.perfilCompleto = data;
        },
        error: (err) => {
          console.error('Error al cargar el perfil completo:', err);
        }
      });

      // Cargar préstamos
      this.prestamoServicio.obtenerPrestamosPorUsuario(usuarioId).subscribe({
        next: (data) => {
          this.prestamos = data;
          console.log('Préstamos:', data);
        },
        error: (err) => {
          console.error('Error al cargar los préstamos:', err);
        }
      });
    } else {
      console.warn('No se encontró el idusuario en el localStorage');
    }
  }

  devolverPrestamo(idPrestamo: number): void {
    this.prestamoServicio.devolverPrestamo(idPrestamo).subscribe({
      next: (msg) => {
        alert(msg);
        // Recargar préstamos
        this.ngOnInit();
      },
      error: (err) => {
        console.error('Error al devolver préstamo:', err);
      }
    });
  }
}

