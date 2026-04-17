package com.plataforma.ReservaEventos.Repository;

import com.plataforma.ReservaEventos.Domain.Evento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    List<Evento> findByActivoTrue();

    List<Evento> findByNombreContainingIgnoreCase(String nombre);
    
    List<Evento> findByActivo(Boolean activo);

    List<Evento> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Evento> findByNombreContainingIgnoreCaseOrderByFechaAsc(String nombre);
    
}
