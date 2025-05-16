package com.example.sistemabiblioteca.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.sistemabiblioteca.persistence.entity.PrestamoEntity;

public interface PrestamoRepository extends CrudRepository<PrestamoEntity, Long>{
    @Query("SELECT p FROM PrestamoEntity p WHERE p.materialEntity.id = :materialId AND p.id_usuario = :usuarioId")
Optional<PrestamoEntity> findByMaterialAndUsuario(@Param("materialId") Long materialId, @Param("usuarioId") Long usuarioId);


@Query("SELECT COUNT(p) FROM PrestamoEntity p WHERE p.id_usuario = :usuarioId AND p.fechaDevolucionReal IS NULL")
int contarPrestamosActivosPorUsuario(@Param("usuarioId") Long usuarioId);

@Query("SELECT p FROM PrestamoEntity p WHERE p.id_usuario = :usuarioId")
List<PrestamoEntity> findPrestamosByUsuarioId(@Param("usuarioId") Long usuarioId);


}
