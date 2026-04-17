package com.plataforma.ReservaEventos.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    // Se recomienda añadir un serialVersionUID
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idRol;

    @Column(name = "nombre", unique = true, length = 100)
    private String rol;
}
