/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plataforma.ReservaEventos.Controller;

import com.plataforma.ReservaEventos.Domain.Evento;
import com.plataforma.ReservaEventos.Service.EventoService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {
    
    private final EventoService eventoService;
    
    public ConsultaController(EventoService eventoService){
        this.eventoService = eventoService;
    }
    
    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("eventos", eventoService.getEventos(false));
        return "/consultas/listado";
    }

    @PostMapping("/consultaDerivada")
    public String consultaDerivada(Model model) {
        model.addAttribute("eventos", eventoService.getEventosActivos());
        model.addAttribute("consultaRealizada", true);
        return "/consultas/listado";
    }

    @PostMapping("/consultaJPQL")
    public String consultaJPQL(@RequestParam("nombre") String nombre, Model model) {
        model.addAttribute("eventos", eventoService.getEventosPorNombreOrdenado(nombre));
        model.addAttribute("consultaRealizada", true);
        return "/consultas/listado";
    }

    @PostMapping("/consultaSQL")
    public String consultaSQL(@RequestParam("inicio") String inicio,
            @RequestParam("fin") String fin,
            Model model) {
        LocalDate fechaInicio = LocalDate.parse(inicio);
        LocalDate fechaFin = LocalDate.parse(fin);
        model.addAttribute("eventos", eventoService.getEventosPorFechas(fechaInicio, fechaFin));
        model.addAttribute("consultaRealizada", true);
        return "/consultas/listado";
    }
    
}
