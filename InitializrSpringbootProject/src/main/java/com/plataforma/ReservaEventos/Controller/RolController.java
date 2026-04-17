package com.plataforma.ReservaEventos.Controller;

import com.plataforma.ReservaEventos.Domain.Rol;
import com.plataforma.ReservaEventos.Service.RolService;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("roles", rolService.getRoles());
        model.addAttribute("rol", new Rol());
        model.addAttribute("totalRoles", rolService.getRoles().size());
        return "/rol/listado";
    }

    @GetMapping("/modificar/{idRol}")
    public String modificar(@PathVariable int idRol, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Rol> rolOpt = rolService.getRol(idRol);
        if (rolOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El rol no fue encontrado.");
            return "redirect:/rol/listado";
        }
        model.addAttribute("rol", rolOpt.get());
        return "/rol/modifica";
    }

    @PostMapping("/guardar")
    public String guardar(Rol rol, RedirectAttributes redirectAttributes) {
        try {
            rolService.save(rol);
            redirectAttributes.addFlashAttribute("exitoso",
                    rol.getIdRol() == 0 ? "Rol creado correctamente."
                                        : "Rol actualizado correctamente.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/rol/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam int idRol, RedirectAttributes redirectAttributes) {
        try {
            rolService.delete(idRol);
            redirectAttributes.addFlashAttribute("exitoso", "Rol eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El rol no existe.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se puede eliminar el rol. Tiene usuarios asociados.");
        }
        return "redirect:/rol/listado";
    }
}
