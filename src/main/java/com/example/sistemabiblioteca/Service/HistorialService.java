package com.example.sistemabiblioteca.Service;

import com.example.sistemabiblioteca.Repository.HistorialRepository;
import com.example.sistemabiblioteca.persistence.entity.HistorialEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository historialRepository;

    public List<HistorialEntity> obtenerHistorialPorUsuario(Long usuarioId) {
        return historialRepository.findHistorialByUsuarioId(usuarioId);
    }

    public List<HistorialEntity> listarTodos() {
        return (List<HistorialEntity>) historialRepository.findAll();
    }
}
