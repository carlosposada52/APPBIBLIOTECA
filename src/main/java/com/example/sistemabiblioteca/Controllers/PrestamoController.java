package com.example.sistemabiblioteca.Controllers;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistemabiblioteca.Repository.MaterialRepository;
// Removed unused MaterialRepository import
import com.example.sistemabiblioteca.Repository.PrestamoRepository;
import com.example.sistemabiblioteca.Service.PrestamoService;
import com.example.sistemabiblioteca.persistence.entity.MaterialEntity;
import com.example.sistemabiblioteca.persistence.entity.PrestamoEntity;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:4200")
public class PrestamoController {
      @Autowired
    private PrestamoRepository prestamoRepository;
      @Autowired
    private MaterialRepository materialRepository;
     @Autowired
    private PrestamoService prestamoService;



   @GetMapping("/prestamos")
    public List<PrestamoEntity> listarTodasLasCategorias() {
        return (List<PrestamoEntity>) prestamoRepository.findAll();
    }


     // metodo para guardar un Material
    @PostMapping("/prestamos")
    public PrestamoEntity guardarPrestamos(@RequestBody PrestamoEntity prestamo) {
        return prestamoRepository.save(prestamo);
    }

    // Metodo para buscar idioma por id
    @GetMapping("/prestamos/{id}")
    public ResponseEntity<PrestamoEntity> obtenerMaterialPorId(@PathVariable Long id) {
        PrestamoEntity prestamo = prestamoRepository.findById(id).orElse(null);
        if (prestamo != null) {
            return ResponseEntity.ok(prestamo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Metodo para actualizar idioma por id



    // metodo para eliminar un prestamo
    @DeleteMapping("prestamos/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Long id) {
        if (prestamoRepository.existsById(id)) {
            prestamoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //metodo para  devolver un material
    
@PostMapping("prestamos/{id_prestamo}/return")
public ResponseEntity<String> devolverMaterial(@PathVariable Long id_prestamo) {
    Optional<PrestamoEntity> prestamoOpt = prestamoRepository.findById(id_prestamo);

    if (!prestamoOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Préstamo no encontrado");
    }

    PrestamoEntity prestamo = prestamoOpt.get();
    MaterialEntity material = prestamo.getMaterialEntity();

    // Sumar 1 al stock disponible
    prestamo.setFechaDevolucionReal(new java.sql.Date(System.currentTimeMillis()));

    material.setSTOCKDISPONIBLE(material.getSTOCKDISPONIBLE() + 1);
    materialRepository.save(material);

    // Eliminar el préstamo 
    prestamoRepository.delete(prestamo);

    return ResponseEntity.ok("Material devuelto correctamente");

}



@GetMapping("/prestamos/usuario/{usuarioId}")
public ResponseEntity<List<PrestamoEntity>> obtenerPrestamosPorUsuario(@PathVariable Long usuarioId) {
    List<PrestamoEntity> prestamos = prestamoService.obtenerPrestamosPorUsuario(usuarioId);
    return ResponseEntity.ok(prestamos);
}



}
