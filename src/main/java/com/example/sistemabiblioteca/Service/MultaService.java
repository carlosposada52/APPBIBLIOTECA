package com.example.sistemabiblioteca.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sistemabiblioteca.Repository.MultaRepository;
import com.example.sistemabiblioteca.Repository.PrestamoRepository;
import com.example.sistemabiblioteca.Repository.UsuarioMoraRepository;
import com.example.sistemabiblioteca.persistence.entity.MultaEntity;
import com.example.sistemabiblioteca.persistence.entity.PrestamoEntity;

import jakarta.transaction.Transactional;

@Service
public class MultaService {

    @Autowired
    private MultaRepository multaRepo;

      @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private UsuarioMoraRepository usuariosMoraRepository;


    @Transactional
    public void pagarMultasPorUsuario(Long idUsuario) {
          List<MultaEntity> multas = multaRepository.findByPrestamoUsuarioId(idUsuario);
    
    if (multas.isEmpty()) {
        throw new RuntimeException("El usuario no tiene multas pendientes");
    }

    multas.forEach(multa -> {
        // Opcional: Actualizar fecha de devolución del préstamo asociado
        PrestamoEntity prestamo = multa.getPrestamo();
        prestamo.setFecha_devolucion(new java.sql.Date(System.currentTimeMillis() + 86400000)); // +1 día
        prestamoRepository.save(prestamo);
        
        multaRepository.delete(multa);
    });
}

 @Transactional
public void eliminarMultasYUsuariosMoraPorUsuario(Long idUsuario) {
    // Obtener IDs de préstamos asociados al usuario
     List<Long> prestamosIds = prestamoRepository.findIdsPrestamoByIdUsuario(idUsuario);

    if (prestamosIds.isEmpty()) {
        return; // No hay préstamos para ese usuario
    }

    // Obtener IDs de multas relacionadas a esos préstamos
    List<Long> multasIds = multaRepository.findIdsMultaByIdPrestamoIn(prestamosIds);

    if (!multasIds.isEmpty()) {
        // Obtener los préstamos relacionados a esas multas antes de eliminarlas
        List<Long> prestamosConMulta = multaRepository.findPrestamoIdsByMultaIds(multasIds);

        // Eliminar relaciones en UsuariosMora
        usuariosMoraRepository.deleteByIdMultaIn(multasIds);

        // Eliminar multas
        multaRepository.deleteByIdPrestamoIn(prestamosIds);

        // Actualizar fecha de devolución solo de los préstamos con multa (hoy + 1 día)
        java.sql.Date nuevaFecha = java.sql.Date.valueOf(LocalDate.now().plusDays(1));
        for (Long idPrestamo : prestamosConMulta) {
            prestamoRepository.actualizarFechaDevolucionPorId(idPrestamo, nuevaFecha);
        }
    }
          
}
}
