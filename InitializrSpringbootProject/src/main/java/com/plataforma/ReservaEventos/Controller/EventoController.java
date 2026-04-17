package com.plataforma.ReservaEventos.Controller;

import com.plataforma.ReservaEventos.Domain.Evento;
import com.plataforma.ReservaEventos.Service.EventoService;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evento")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var eventos = eventoService.getEventos(false);
        model.addAttribute("eventos", eventos);
        model.addAttribute("totalEventos", eventos.size());
        model.addAttribute("evento", new Evento());
        return "/evento/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("evento", new Evento());
        return "/evento/modifica";
    }

    @GetMapping("/modificar/{idEvento}")
    public String modificar(@PathVariable int idEvento, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Evento> eventoOpt = eventoService.getEvento(idEvento);
        if (eventoOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El evento no fue encontrado.");
            return "redirect:/evento/listado";
        }
        model.addAttribute("evento", eventoOpt.get());
        return "/evento/modifica";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Evento evento, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Por favor, corrija los errores en el formulario.");
            if (evento.getIdEvento() == 0) {
                return "redirect:/evento/nuevo";
            }
            return "redirect:/evento/modificar/" + evento.getIdEvento();
        }
        eventoService.save(evento);
        redirectAttributes.addFlashAttribute("exitoso",
                evento.getIdEvento() == 0 ? "Evento creado correctamente."
                                    : "Evento actualizado correctamente.");
        return "redirect:/evento/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam int idEvento,
            RedirectAttributes redirectAttributes) {
        try {
            eventoService.delete(idEvento);
            redirectAttributes.addFlashAttribute("exitoso", "Evento eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El evento no existe.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el evento. Tiene datos asociados.");
        }
        return "redirect:/evento/listado";
    }
}
