package com.example.sistemabiblioteca.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sistemabiblioteca.Model.UsuarioModel;
import com.example.sistemabiblioteca.Repository.MaterialRepository;
import com.example.sistemabiblioteca.Repository.PrestamoRepository;
import com.example.sistemabiblioteca.Repository.UsuarioRepository;
import com.example.sistemabiblioteca.persistence.entity.MaterialEntity;
import com.example.sistemabiblioteca.persistence.entity.PrestamoEntity;

@Service
public class PrestamoService {

     @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
private UsuarioRepository usuarioRepository;

//funcion para prestar material

 public void prestarMaterial(Long materialId, Long usuarioId) {
    MaterialEntity material = materialRepository.findById(materialId)
        .orElseThrow(() -> new RuntimeException("Material no encontrado"));

    if (material.getSTOCKDISPONIBLE().intValue() <= 0) {
        throw new RuntimeException("No hay stock disponible");
    }

    UsuarioModel usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String tipo = usuario.getTipo().toUpperCase();

    // Si es estudiante, verificar límite de 5 préstamos activos
    if (tipo.equals("ESTUDIANTE")) {
        int prestamosActivos = prestamoRepository.contarPrestamosActivosPorUsuario(usuarioId);
        if (prestamosActivos >= 5) {
            throw new RuntimeException("El estudiante ya tiene 5 préstamos activos y no puede realizar más.");
        }
    }

    // Descontar stock
    material.setSTOCKDISPONIBLE(material.getSTOCKDISPONIBLE() - 1);
    materialRepository.save(material);

    // Calcular fecha de devolución
    java.util.Date now = new java.util.Date();
    java.util.Date fechaDevolucion;
    if (tipo.equals("ESTUDIANTE")) {
        fechaDevolucion = new java.util.Date(now.getTime() + 5L * 24 * 60 * 60 * 1000); // 5 días
    } else {
        fechaDevolucion = new java.util.Date(now.getTime() + 90L * 24 * 60 * 60 * 1000); // 3 meses
    }

    // Crear y guardar el préstamo
    PrestamoEntity prestamo = new PrestamoEntity();
    prestamo.setId_usuario(usuarioId);
    prestamo.setMaterialEntity(material);
    prestamo.setFecha_prestamo(new java.sql.Date(now.getTime()));
    prestamo.setFecha_devolucion(new java.sql.Date(fechaDevolucion.getTime()));
    prestamoRepository.save(prestamo);
}

    public void devolverMaterial(Long id_material, Long usuarioId) {
    PrestamoEntity prestamo = prestamoRepository
        .findByMaterialAndUsuario(id_material, usuarioId)
        .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

    MaterialEntity material = prestamo.getMaterialEntity();
    material.setSTOCKDISPONIBLE(material.getSTOCKDISPONIBLE() + 1);

    materialRepository.save(material);
    prestamoRepository.delete(prestamo); // Esto activará el trigger en Oracle
}


public List<PrestamoEntity> obtenerPrestamosPorUsuario(Long usuarioId) {
    return prestamoRepository.findPrestamosByUsuarioId(usuarioId);
}


}
