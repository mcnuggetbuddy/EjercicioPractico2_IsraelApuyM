/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.plataforma.ReservaEventos.Controller;


import com.plataforma.ReservaEventos.Domain.Usuario;
import com.plataforma.ReservaEventos.Service.UsuarioService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 *
 * @author israelapuy
 */
@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    @Autowired
    private UsuarioService usuarioService;

    // 1. Endpoint para la vista inicial (sin usuario)
    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuario", new Usuario());
        // Se inicializan listas vacías para evitar errores de Thymeleaf
        model.addAttribute("rolesAsignados", Collections.emptySet());
        model.addAttribute("rolesDisponibles", Collections.emptyList());
        return "usuario_rol/mantenimiento";
    }

    // 2. Endpoint para buscar y mostrar roles
    @GetMapping("/buscar")
    public String buscarUsuario(@RequestParam("email") String email, Model model) {
        // Usa el método existente en UsuarioService
        Usuario usuario = usuarioService.getUsuarioPorEmail(email).orElse(null);

        model.addAttribute("usuario", usuario);

        if (usuario != null) {
            // Obtener todos los nombres de roles
            List<String> todosRolesNombres = usuarioService.getRolesNombres();

            // Filtrar los roles disponibles (los que no tiene el usuario)
            List<String> rolesDisponibles = todosRolesNombres.stream()
                    .filter(rolNombre -> usuario.getRoles().stream()
                    .noneMatch(rolAsignado -> rolAsignado.getRol().equals(rolNombre)))
                    .toList();

            model.addAttribute("rolesAsignados", usuario.getRoles());
            model.addAttribute("rolesDisponibles", rolesDisponibles);
        }

        return "usuario_rol/mantenimiento"; // Vuelve a la misma página
    }

    // 3. Endpoint para agregar un rol
    @GetMapping("/agregar")
    public String agregarRol(@RequestParam("email") String email,
            @RequestParam("nombreRol") String nombreRol) {

        usuarioService.asignarRolPorEmail(email, nombreRol);

        // Redirige al /buscar para recargar los datos del usuario actualizado
        return "redirect:/usuario_rol/buscar?email=" + email;
    }

    // 4. Endpoint para eliminar un rol
    @GetMapping("/eliminar")
    public String eliminarRol(@RequestParam("email") String email,
            @RequestParam("idRol") Integer idRol) {

        usuarioService.eliminarRol(email, idRol);

        // Redirige al /buscar para recargar los datos del usuario actualizado
        return "redirect:/usuario_rol/buscar?email=" + email;
    }
}
