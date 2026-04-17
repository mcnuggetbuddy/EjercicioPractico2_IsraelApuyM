package com.plataforma.ReservaEventos.Domain;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int idUsuario;

    @Column(length = 150)
    private String nombre;

    @Column(unique = true, length = 200)
    private String email;

    @Column(length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean activo = true;

    @Column(name = "fecha_creacion", updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaCreacion;

    // Relación Many-to-Many con la entidad Rol
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();
}
