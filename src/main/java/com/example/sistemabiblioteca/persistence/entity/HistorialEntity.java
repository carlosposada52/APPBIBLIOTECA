package com.example.sistemabiblioteca.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "HISTORIAL_PRESTAMO")
public class HistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_seq")
    @SequenceGenerator(name = "historial_seq", sequenceName = "HISTORIAL_SEQ", allocationSize = 1)
    @Column(name = "ID_HISTORIAL")
    private Long idHistorial;

    @Column(name = "ID_PRESTAMO")  
    private Long prestamo; 

    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_MATERIAL")  
    private MaterialEntity material; 

    @Column(name = "FECHA_PRESTAMO")
    private LocalDate fechaPrestamo;

    @Column(name = "FECHA_DEVOLUCION")
    private LocalDate fechaDevolucion;

    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;
}