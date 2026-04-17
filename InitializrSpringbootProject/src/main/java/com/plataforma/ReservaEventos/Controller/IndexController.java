package com.plataforma.ReservaEventos.Controller;

import com.plataforma.ReservaEventos.Service.EventoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final EventoService eventoService;

    public IndexController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("eventos", eventoService.getEventos(true));
        return "index";
    }
}
