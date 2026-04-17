package com.plataforma.ReservaEventos.Controller;

import com.plataforma.ReservaEventos.Domain.Usuario;
import com.plataforma.ReservaEventos.Service.RegistroService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private final RegistroService registroService;

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    // Muestra el formulario de registro
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/nuevo";
    }

    // Procesa el formulario de registro y envía correo de activación
    @PostMapping("/crearUsuario")
    public String crearUsuario(Usuario usuario, Model model) throws MessagingException {
        registroService.crearUsuario(model, usuario);
        return "registro/salida";
    }

    // Muestra el formulario de activación (enlace del correo)
    @GetMapping("/activar")
    public String activar(@RequestParam String username,
                          @RequestParam String clave,
                          Model model) {
        registroService.activar(model, username, clave);
        return "registro/activa";
    }

    // Procesa el formulario de activación final
    @PostMapping("/activar")
    public String activar(Usuario usuario, Model model) {
        registroService.activar(usuario);
        model.addAttribute("titulo", "Cuenta activada");
        model.addAttribute("mensaje", "Su cuenta ha sido activada correctamente. Ya puede iniciar sesión.");
        return "registro/salida";
    }

    // Muestra el formulario para recordar contraseña
    @GetMapping("/recordarUsuario")
    public String recordarUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro/recordar";
    }

    // Procesa la solicitud de recordar contraseña
    @PostMapping("/recordarUsuario")
    public String recordarUsuario(Usuario usuario, Model model) throws MessagingException {
        registroService.recordarUsuario(model, usuario);
        return "registro/salida";
    }
}
