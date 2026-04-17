package com.plataforma.ReservaEventos.Service;

import com.plataforma.ReservaEventos.Domain.Rol;
import com.plataforma.ReservaEventos.Repository.RolRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<Rol> getRoles() {
        return rolRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Rol> getRol(int id) {
        return rolRepository.findById(id);
    }

    @Transactional
    public void save(Rol rol) {
        // Verificar nombre duplicado excluyendo el registro actual
        Optional<Rol> duplicado = rolRepository.findByRol(rol.getRol());
        if (duplicado.isPresent() && duplicado.get().getIdRol() != rol.getIdRol()) {
            throw new DataIntegrityViolationException("Ya existe un rol con ese nombre.");
        }
        rolRepository.save(rol);
    }

    @Transactional
    public void delete(int id) {
        if (!rolRepository.existsById(id)) {
            throw new IllegalArgumentException("El rol con ID " + id + " no existe.");
        }
        try {
            rolRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el rol. Tiene usuarios asociados.", e);
        }
    }
}
