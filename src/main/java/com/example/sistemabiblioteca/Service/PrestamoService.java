package com.example.sistemabiblioteca.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sistemabiblioteca.Model.PrestamoDTO;
import com.example.sistemabiblioteca.Model.PrestamoMultaDTO;
import com.example.sistemabiblioteca.Model.UsuarioModel;
import com.example.sistemabiblioteca.Repository.MaterialRepository;
import com.example.sistemabiblioteca.Repository.MultaRepository;
import com.example.sistemabiblioteca.Repository.PrestamoRepository;
import com.example.sistemabiblioteca.Repository.UsuarioMoraRepository;
import com.example.sistemabiblioteca.Repository.UsuarioRepository;
import com.example.sistemabiblioteca.persistence.entity.MaterialEntity;
import com.example.sistemabiblioteca.persistence.entity.MultaEntity;
import com.example.sistemabiblioteca.persistence.entity.PrestamoEntity;
import java.util.Optional;

@Service
public class PrestamoService {

     @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMoraRepository usuarios_moraRepo;

    @Autowired
    private MultaRepository multaRepo;

    @Autowired
    private UsuarioMoraService usuarioMoraService;

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
    } else{
        fechaDevolucion = new java.util.Date(now.getTime() + 90L * 24 * 60 * 60 * 1000); // 3 meses
    }

    // Crear y guardar el préstamo
    PrestamoEntity prestamo = new PrestamoEntity();
    prestamo.setUsuario(usuario);
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

   public List<PrestamoDTO> obtenerPrestamosConEstado(Long usuarioId) {
           List<PrestamoEntity> prestamos = prestamoRepository.findPrestamosByUsuarioId(usuarioId);
              // Fecha actual para la consulta de penalización
    Date fechaActual = new Date();

  
     // Chequear si el usuario tiene mora activa
    boolean tienePenalizacion = usuarioMoraService.tieneMoraActiva(usuarioId);

    List<PrestamoDTO> resultado = new ArrayList<>();

    for (PrestamoEntity prestamo : prestamos) {
        Long idPrestamo = prestamo.getId_Prestamo();
        Date fechaDevolucion = prestamo.getFecha_devolucion();
        Date fechaDevolucionReal = prestamo.getFechaDevolucionReal();
        Date fechaPrestamo = prestamo.getFecha_prestamo();

        // Comparar fecha actual si aún no se ha devuelto
        Date fechaComparacion = (fechaDevolucionReal != null) ? fechaDevolucionReal : new Date();
        boolean estaRetrasado = fechaComparacion.after(fechaDevolucion);

        // Consultar si tiene multa y si está penalizado
        boolean tieneMulta = multaRepo.existsByIdPrestamo(idPrestamo);
        
       
        Double montoMulta = 0.0;
        int dias_retraso=0;
          Optional<MultaEntity> multa = multaRepo.findByIdPrestamo(idPrestamo);
            if (multa.isPresent()) {
                MultaEntity multaEntity = multa.get();
                montoMulta = multaEntity.getMonto();
                dias_retraso = (multaEntity.getDiasRetraso() != null) ? multaEntity.getDiasRetraso() : 0;
            
            }
        PrestamoDTO dto = new PrestamoDTO();
        dto.setIdPrestamo(idPrestamo);
        dto.setNombreUsuario(prestamo.getUsuario().getNombre());
        dto.setFechaDevolucion(fechaDevolucion);
        dto.setFechaDevolucionReal(fechaDevolucionReal);
        dto.setFechaPrestamo(fechaPrestamo);
        dto.setEstaRetrasado(estaRetrasado);
        dto.setTieneMulta(tieneMulta);
          // Puedes agregar esta info al DTO, por ejemplo:
        dto.setPenalizado(tienePenalizacion);
        dto.setTituloMaterial(prestamo.getMaterialEntity() != null ? prestamo.getMaterialEntity().getTitulo() : null);
        dto.setMonto(montoMulta);
        dto.setDias_retraso(dias_retraso);
        resultado.add(dto);
    }

    return resultado;
}



}
