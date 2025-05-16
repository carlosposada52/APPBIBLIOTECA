import { Component } from '@angular/core';
import { Material } from '../material';
import { MaterialServiceService } from '../material-service.service';
import { Router } from '@angular/router';
import { PerfilDTO } from '../dto/perfil-dto';
import { PerfilService } from '../services/perfil.service';

@Component({
  selector: 'app-prestar-material',
  standalone: false,
  templateUrl: './prestar-material.component.html',
  styleUrl: './prestar-material.component.css'
})
export class PrestarMaterialComponent {

   filtroBusqueda: string = '';
 
  p: number = 1;  
  itemsPerPage: number = 5;
  materiales: Material[];
  perfilCompleto!: PerfilDTO;

  constructor(private Materialservicio: MaterialServiceService, private route: Router, private usuarioService:PerfilService) {

  }

  ngOnInit(): void {
    this.obtenerMateriales();
    const id = localStorage.getItem('idusuario');

    if (id) {
      this.usuarioService.getPerfil(Number(id)).subscribe({
        next: (data) => {
          console.log('Expediente completo:', data);
          this.perfilCompleto = data;
        },
        error: (err) => {
          console.error('Error al cargar el perfil completo:', err);
        }
      });
    } else {
      console.warn('No se encontró el idusuario en el localStorage');
    }
  }

  obtenerMateriales() {
    this.Materialservicio.obtenerListaMateriales().subscribe(dato => {
      this.materiales = dato;
    });
  }


   verDetallesMaterial(id: number) {
    this.route.navigate(['borrow_material', id]);
    
  }

  //metodo para buscar material
   materialesFiltrados(): Material[] {
    if (!this.filtroBusqueda.trim()) {
      return this.materiales;
    }

    const filtro = this.filtroBusqueda.toLowerCase();

    const filtrados = this.materiales.filter(material =>
      material.titulo?.toLowerCase().includes(filtro) ||
      material.autor?.toLowerCase().includes(filtro) ||
      material.tipomaterial?.toLowerCase().includes(filtro) ||
      material.aniopublicacion?.toString().includes(filtro) ||
      material.stockdisponible?.toString().includes(filtro) ||
      material.idCategoria?.nombreCategoria?.toLowerCase().includes(filtro)
    );

    // Paginación: Solo devolver los elementos de la página actual
    return filtrados.slice((this.p - 1) * this.itemsPerPage, this.p * this.itemsPerPage);
  }

//metodo para 
prestar(materialId: number) {
  const idUsuario = localStorage.getItem('idusuario');

  if (!idUsuario) {
    console.warn('No se encontró el ID de usuario en localStorage');
    return;
  }

  this.Materialservicio.prestarMaterial(materialId, Number(idUsuario)).subscribe({
    next: (response) => {
      console.log('Material prestado exitosamente', response);
      alert('Material prestado correctamente');
      this.obtenerMateriales(); // Actualizar lista de materiales
    },
    error: (err) => {
      console.error('Error al prestar el material', err);
      alert('Error al prestar el material');
    }
  });

  
}






}
