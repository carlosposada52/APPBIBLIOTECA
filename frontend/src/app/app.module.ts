import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { RestapiService } from './restapi.service';
import { HttpClientModule } from '@angular/common/http';
import { MatOptionModule } from '@angular/material/core'; 
import { LayoutComponent } from './layout/layout.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { AgregarUsuariosComponent } from './agregar-usuarios/agregar-usuarios.component';
import { ListaUsuariosComponent } from './lista-usuarios/lista-usuarios.component';
import { ActualizarUsuariosComponent } from './actualizar-usuarios/actualizar-usuarios.component';
import { UsuarioDetallesComponent } from './usuario-detalles/usuario-detalles.component';
import { CrearRolComponent } from './crear-rol/crear-rol.component';
import { AgregarUsuarioBiblioComponent } from './agregar-usuario-biblio/agregar-usuario-biblio.component';
import { ActualizarUsuarioBiblioComponent } from './actualizar-usuario-biblio/actualizar-usuario-biblio.component';
import { PerfilComponent } from './perfil/perfil.component';
import { DashboardUsuarioComponent } from './dashboard-usuario/dashboard-usuario.component';
import { ListaIdiomasComponent } from './lista-idiomas/lista-idiomas.component';
import { AgregarIdiomasComponent } from './agregar-idiomas/agregar-idiomas.component';
import { ActualizarIdiomasComponent } from './actualizar-idiomas/actualizar-idiomas.component';
import { IdiomaDetalleComponent } from './idioma-detalle/idioma-detalle.component';
import { ListaMaterialesComponent } from './lista-materiales/lista-materiales.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { AgregarMaterialesComponent } from './agregar-materiales/agregar-materiales.component';
import { ActualizarMaterialesComponent } from './actualizar-materiales/actualizar-materiales.component';
import { MaterialDetalleComponent } from './material-detalle/material-detalle.component';
import { PrestarMaterialComponent } from './prestar-material/prestar-material.component';
import { DevolverPrestamoComponent } from './devolver-prestamo/devolver-prestamo.component';
import { MisPrestamosComponent } from './mis-prestamos/mis-prestamos.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    LayoutComponent,
    AgregarUsuariosComponent,
    ListaUsuariosComponent,
    ActualizarUsuariosComponent,
    UsuarioDetallesComponent,
    CrearRolComponent,
    AgregarUsuarioBiblioComponent,
    ActualizarUsuarioBiblioComponent,
    PerfilComponent,
    DashboardUsuarioComponent,
    ListaIdiomasComponent,
    AgregarIdiomasComponent,
    ActualizarIdiomasComponent,
    IdiomaDetalleComponent,
    ListaMaterialesComponent,
    AgregarMaterialesComponent,
    ActualizarMaterialesComponent,
    MaterialDetalleComponent,
    PrestarMaterialComponent,
    DevolverPrestamoComponent,
    MisPrestamosComponent
  
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
   ReactiveFormsModule,
   NgxPaginationModule,
  ],
  providers: [RestapiService],
  bootstrap: [AppComponent]
})
export class AppModule { }
