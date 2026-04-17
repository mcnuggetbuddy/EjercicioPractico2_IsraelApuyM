/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plataforma.ReservaEventos.Repository;

import com.plataforma.ReservaEventos.Domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Optional<Usuario> findByEmailAndActivoTrue(String email);

    public List<Usuario> findByActivoTrue();
    
    public Optional<Usuario> findByNombre(String nombre);

    public Optional<Usuario> findByEmail(String email);

    public boolean existsByEmail(String email);
    
    public Optional<Usuario> findByEmailAndPassword(String email, String Password);
    
    public Optional<Usuario> findByNombreOrEmail(String nombre, String email);
    
    public boolean existsByNombreOrEmail(String nombre, String email);


}
