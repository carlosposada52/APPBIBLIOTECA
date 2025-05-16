package com.example.sistemabiblioteca.persistence.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prestamo")
public class PrestamoEntity {

      @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_Prestamo;

    @Column(name = "id_usuario", nullable = false)
    private Long id_usuario;

     @ManyToOne
    @JoinColumn(name = "id_material")  
    private MaterialEntity materialEntity; 

    @Column(name = "fecha_prestamo", nullable = false)
    private Date fecha_prestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    private Date fecha_devolucion;

    @Column(name = "FECHA_DEVOLUCION_REAL")
private Date fechaDevolucionReal;


    
}
