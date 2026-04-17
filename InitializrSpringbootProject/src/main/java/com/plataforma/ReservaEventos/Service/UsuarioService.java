/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plataforma.ReservaEventos.Service;

import com.plataforma.ReservaEventos.Domain.Rol;
import com.plataforma.ReservaEventos.Domain.Usuario;
import com.plataforma.ReservaEventos.Repository.RolRepository;
import com.plataforma.ReservaEventos.Repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author israelapuy
 */
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getUsuarios(boolean activo) {
        if (activo) {
            return usuarioRepository.findByActivoTrue();
        }
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(int idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorNombreOEmail(String nombre,
            String email) {
        return usuarioRepository.findByNombreOrEmail(nombre, email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuarioPorEmailYPassword(String email,
            String password) {
        return usuarioRepository.findByEmailAndPassword(email, password);
    }
    
    @Transactional
    public void save(Usuario usuario, boolean encriptaClave) {
        // Verificar si el correo ya existe, excluyendo el usuario actual        
        final int idUser = usuario.getIdUsuario();
        Optional<Usuario> usuarioDuplicado = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioDuplicado.isPresent()) {
            Usuario encontrado = usuarioDuplicado.get();

            // Verifica si estamos en modo CREACIÓN (idUser == 0) O si el ID encontrado NO es el mismo que estamos actualizando
            if (idUser == 0 || encontrado.getIdUsuario() != idUser) {
                throw new DataIntegrityViolationException("El correo ya está en uso por otro usuario.");
            }
        }
        //Se valida si la clave se va actualizar o si es un usuario nuevo se debe actualizar...
        var asignarRol = false;
        if (usuario.getIdUsuario() == 0) {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                throw new IllegalArgumentException("La contraseña es obligatoria para nuevos usuarios.");
            }
            //La primera vez como es activación no se encripta...
            usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword());
            asignarRol = true;
        } else {
            if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
                // El campo de password en el formulario viene vacío (no se desea actualizar).
                // Recuperamos la contraseña HASHED existente de la base de datos.
                Usuario usuarioExistente = usuarioRepository.findById(usuario.getIdUsuario())
                        .orElseThrow(() -> new IllegalArgumentException("Usuario a modificar no encontrado."));

                // Asignamos la contraseña existente al objeto "usuario" antes de guardarlo.                
                usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuarioExistente.getPassword()) : usuarioExistente.getPassword());
            } else {
                // El campo de password NO está vacío (se desea actualizar).
                // Se encripta y se guarda la nueva contraseña.
                usuario.setPassword(encriptaClave ? passwordEncoder.encode(usuario.getPassword()) : usuario.getPassword());
            }
        }
        if (asignarRol && usuario.getRol() == null) {
            // Asignar rol CLIENTE por defecto en el campo ManyToOne
            rolRepository.findByRol("CLIENTE").ifPresent(usuario::setRol);
        }
        usuario = usuarioRepository.save(usuario);

        if (asignarRol) {
            // Asignar rol CLIENTE por defecto en la tabla ManyToMany usuario_rol
            asignarRolPorEmail(usuario.getEmail(), "CLIENTE");
        }
    }
    
    @Transactional
    public void delete(int idUsuario) {
        // Verifica si la categoría existe antes de intentar eliminarlo
        if (!usuarioRepository.existsById(idUsuario)) {
            // Lanza una excepción para indicar que el usuario no fue encontrado
            throw new IllegalArgumentException(
                    "El usuario con ID " + idUsuario + " no existe.");
        }
        try {
            usuarioRepository.deleteById(idUsuario);
        } catch (DataIntegrityViolationException e) {
            // Excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException(
                    "No se puede eliminar el usuario. Tiene datos asociados.", e);
        }
    }

    @Transactional
    public Usuario asignarRolPorEmail(String email, String rolStr) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado: " + email);
        }
        Usuario usuario = usuarioOpt.get();
        Optional<Rol> rolOpt = rolRepository.findByRol(rolStr);
        if (rolOpt.isEmpty()) {
            throw new RuntimeException("Rol no encontrado.");
        }
        Rol rol = rolOpt.get();
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }

    //Sección para gestionar roles a usuarios...
    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<String> getRolesNombres() {
        return rolRepository.findAll().stream()
                .map(Rol::getRol)
                .toList();
    }

    @Transactional
    public Usuario eliminarRol(String email, int idRol) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado: " + email);
        }
        Usuario usuario = usuarioOpt.get();

        // Filtra la colección de roles del usuario para mantener solo los que NO coinciden con idRol
        usuario.getRoles().removeIf(rol -> rol.getIdRol() == idRol);

        // Guarda el usuario con la colección de roles modificada
        return usuarioRepository.save(usuario);
    }
    
}
