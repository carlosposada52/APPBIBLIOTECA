package com.example.sistemabiblioteca.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.sistemabiblioteca.persistence.entity.HistorialEntity;

public interface HistorialRepository extends CrudRepository<HistorialEntity, Long> {

    
@Query("SELECT h FROM HistorialEntity h WHERE h.idUsuario = :usuarioId")
List<HistorialEntity> findHistorialByUsuarioId(@Param("usuarioId") Long usuarioId);



}
