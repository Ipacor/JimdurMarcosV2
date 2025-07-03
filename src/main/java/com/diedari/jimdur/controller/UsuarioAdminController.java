package com.diedari.jimdur.controller;

import com.diedari.jimdur.model.security.Usuario;
import com.diedari.jimdur.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('LEER_USUARIOS')")
public class UsuarioAdminController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listar(Model model) {
        List<Usuario> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("claseActiva", "usuarios");
        return "admin/usuarios/listar";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasAuthority('CREAR_USUARIOS')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", usuarioService.getRolesDisponibles());
        model.addAttribute("claseActiva", "usuarios");
        return "admin/usuarios/nuevo";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasAuthority('CREAR_USUARIOS')")
    public String guardar(@Valid @ModelAttribute Usuario usuario,
                         BindingResult result,
                         @RequestParam(required = false) Set<Long> roles,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("roles", usuarioService.getRolesDisponibles());
            model.addAttribute("claseActiva", "usuarios");
            return "admin/usuarios/nuevo";
        }

        // Verificar si el email ya existe
        if (usuarioService.existsByEmail(usuario.getEmail())) {
            result.rejectValue("email", "error.usuario", "Ya existe un usuario con este email");
            model.addAttribute("roles", usuarioService.getRolesDisponibles());
            model.addAttribute("claseActiva", "usuarios");
            return "admin/usuarios/nuevo";
        }

        try {
            // Encriptar contraseña
            usuario.setContrasenaHash(passwordEncoder.encode(usuario.getContrasenaHash()));
            usuario.setFechaRegistro(LocalDateTime.now());
            usuario.setEstadoCuenta("ACTIVO");

            Usuario usuarioGuardado = usuarioService.save(usuario);

            // Asignar roles si se proporcionaron
            if (roles != null && !roles.isEmpty()) {
                usuarioService.asignarRoles(usuarioGuardado.getId(), roles);
            }

            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear el usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAuthority('EDITAR_USUARIOS')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return usuarioService.findById(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("roles", usuarioService.getRolesDisponibles());
                    model.addAttribute("claseActiva", "usuarios");
                    return "admin/usuarios/editar";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/admin/usuarios";
                });
    }

    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasAuthority('EDITAR_USUARIOS')")
    public String actualizar(@PathVariable Long id,
                           @Valid @ModelAttribute Usuario usuario,
                           BindingResult result,
                           @RequestParam(required = false) Set<Long> roles,
                           @RequestParam(required = false) String nuevaContrasena,
                           RedirectAttributes redirectAttributes,
                           Model model) {

        if (result.hasErrors()) {
            model.addAttribute("roles", usuarioService.getRolesDisponibles());
            model.addAttribute("claseActiva", "usuarios");
            return "admin/usuarios/editar";
        }

        // Verificar si el email ya existe para otro usuario
        if (usuarioService.existsByEmailAndIdNot(usuario.getEmail(), id)) {
            result.rejectValue("email", "error.usuario", "Ya existe otro usuario con este email");
            model.addAttribute("roles", usuarioService.getRolesDisponibles());
            model.addAttribute("claseActiva", "usuarios");
            return "admin/usuarios/editar";
        }

        try {
            usuario.setId(id);

            // Si se proporciona una nueva contraseña, encriptarla
            if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                usuario.setContrasenaHash(passwordEncoder.encode(nuevaContrasena));
            } else {
                // Mantener la contraseña anterior
                Usuario usuarioExistente = usuarioService.findById(id).orElseThrow();
                usuario.setContrasenaHash(usuarioExistente.getContrasenaHash());
            }

            Usuario usuarioActualizado = usuarioService.save(usuario);

            // Actualizar roles
            usuarioService.asignarRoles(usuarioActualizado.getId(), roles != null ? roles : Set.of());

            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar el usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }

        return "redirect:/admin/usuarios";
    }

    @PostMapping("/activar/{id}")
    @PreAuthorize("hasAuthority('EDITAR_USUARIOS')")
    public String activar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.activar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario activado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al activar el usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/desactivar/{id}")
    @PreAuthorize("hasAuthority('DESACTIVAR_USUARIOS')")
    public String desactivar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al desactivar el usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAuthority('EDITAR_USUARIOS')")
    public String mostrarRoles(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return usuarioService.findById(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("todosLosRoles", usuarioService.getRolesDisponibles());
                    model.addAttribute("claseActiva", "usuarios");
                    return "admin/usuarios/roles";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensaje", "Usuario no encontrado");
                    redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                    return "redirect:/admin/usuarios";
                });
    }

    @PostMapping("/asignar-roles/{id}")
    @PreAuthorize("hasAuthority('EDITAR_USUARIOS')")
    public String asignarRoles(@PathVariable Long id,
                             @RequestParam(required = false) Set<Long> roles,
                             RedirectAttributes redirectAttributes) {
        try {
            usuarioService.asignarRoles(id, roles != null ? roles : Set.of());
            redirectAttributes.addFlashAttribute("mensaje", "Roles asignados exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al asignar roles: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/usuarios";
    }

    // API REST para uso con JavaScript
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Usuario>> listarApi() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> obtenerApi(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 