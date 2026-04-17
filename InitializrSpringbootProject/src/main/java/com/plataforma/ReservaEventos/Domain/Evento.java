package com.plataforma.ReservaEventos.Domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.io.Serializable;


@Data
@Entity
@Table(name = "evento")
public class Evento implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idEvento;

    @Column(length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDate fecha;

    private Integer capacidad;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;
}
