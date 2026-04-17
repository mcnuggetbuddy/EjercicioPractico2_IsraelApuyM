package com.plataforma.ReservaEventos.Service;

import com.plataforma.ReservaEventos.Domain.Evento;
import com.plataforma.ReservaEventos.Repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Transactional(readOnly = true)
    public List<Evento> getEventos(boolean soloActivos) {
        if (soloActivos) {
            return eventoRepository.findByActivoTrue();
        }
        return eventoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Evento> getEvento(int id) {
        return eventoRepository.findById(id);
    }

    @Transactional
    public void save(Evento evento) {
        eventoRepository.save(evento);
    }

    @Transactional
    public void delete(int id) {
        if (!eventoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El evento con ID " + id + " no existe.");
        }
        try {
            eventoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el evento. Tiene datos asociados.", e);
        }
    }
    
    @Transactional
    public List<Evento> getEventos() {
        return eventoRepository.findAll();
    }

    @Transactional
    public List<Evento> getEventosActivos() {
        return eventoRepository.findByActivoTrue();
    }

    @Transactional
    public List<Evento> getEventosPorEstado(Boolean activo) {
        return eventoRepository.findByActivo(activo);
    }

    @Transactional
    public List<Evento> getEventosPorNombre(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public List<Evento> getEventosPorNombreOrdenado(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCaseOrderByFechaAsc(nombre);
    }

    @Transactional
    public List<Evento> getEventosPorFechas(LocalDate inicio, LocalDate fin) {
        return eventoRepository.findByFechaBetween(inicio, fin);
    }
}
