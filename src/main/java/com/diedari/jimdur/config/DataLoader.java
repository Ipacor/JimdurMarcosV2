package com.diedari.jimdur.config;

import com.diedari.jimdur.model.security.Permiso;
import com.diedari.jimdur.model.security.Rol;
import com.diedari.jimdur.model.security.Usuario;
import com.diedari.jimdur.repository.security.PermisoRepository;
import com.diedari.jimdur.repository.security.RolRepository;
import com.diedari.jimdur.repository.security.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.diedari.jimdur.model.business.UsuarioRef;
import com.diedari.jimdur.repository.business.UsuarioRefRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
// @RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRefRepository usuarioRefRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (rolRepository.count() > 0 || usuarioRepository.count() > 0 || permisoRepository.count() > 0) {
            System.out.println("La base de datos ya contiene datos de seguridad, no se cargaron datos iniciales.");
            return;
        }

        System.out.println("Cargando datos iniciales (permisos, roles y usuarios)...");

        // --- 1. Crear todos los Permisos ---
        Set<Permiso> todosLosPermisos = Arrays.stream(new String[]{
            "LEER_CATEGORIAS", "CREAR_CATEGORIAS", "EDITAR_CATEGORIAS", "DESACTIVAR_CATEGORIAS",
            "LEER_MARCAS", "CREAR_MARCAS", "EDITAR_MARCAS", "DESACTIVAR_MARCAS",
            "LEER_PRODUCTOS", "CREAR_PRODUCTOS", "EDITAR_PRODUCTOS", "DESACTIVAR_PRODUCTOS",
            "LEER_PROVEEDORES", "CREAR_PROVEEDORES", "EDITAR_PROVEEDORES", "DESACTIVAR_PROVEEDORES",
            "LEER_USUARIOS", "CREAR_USUARIOS", "EDITAR_USUARIOS", "DESACTIVAR_USUARIOS",
            "LEER_UBICACIONES", "CREAR_UBICACIONES", "EDITAR_UBICACIONES", "DESACTIVAR_UBICACIONES",
            "LEER_ORDENES", "CREAR_ORDENES", "APROBAR_ORDENES", "CANCELAR_ORDENES",
            "GESTIONAR_ROLES", "GESTIONAR_PERMISOS", "VER_DASHBOARD"
        }).map(nombre -> {
            Permiso p = new Permiso();
            p.setNombre(nombre);
            p.setDescripcion("Permiso para " + nombre.toLowerCase().replace("_", " "));
            return p;
        }).collect(Collectors.toSet());
        
        permisoRepository.saveAll(todosLosPermisos);

        // --- 2. Crear los Roles y asignarles Permisos ---
        
        // Permisos para ADMIN (todos los permisos)
        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ROLE_ADMIN");
        rolAdmin.setDescripcion("Administrador del sistema con todos los permisos");
        rolAdmin.setPermisos(todosLosPermisos);
        rolAdmin.setActivo(true);

        // Permisos para USER (solo lectura y crear órdenes)
        Set<Permiso> permisosUser = todosLosPermisos.stream()
            .filter(p -> p.getNombre().startsWith("LEER_") || p.getNombre().equals("CREAR_ORDENES"))
            .collect(Collectors.toSet());

        Rol rolUser = new Rol();
        rolUser.setNombre("ROLE_USER");
        rolUser.setDescripcion("Usuario cliente con permisos limitados");
        rolUser.setPermisos(permisosUser);
        rolUser.setActivo(true);

        // Permisos para PROVEEDOR (gestión de productos y proveedores)
        Set<Permiso> permisosProveedor = todosLosPermisos.stream()
            .filter(p -> p.getNombre().contains("PRODUCTOS") || 
                        p.getNombre().contains("PROVEEDORES") ||
                        p.getNombre().contains("MARCAS") ||
                        p.getNombre().contains("CATEGORIAS") ||
                        p.getNombre().equals("LEER_ORDENES"))
            .collect(Collectors.toSet());

        Rol rolProveedor = new Rol();
        rolProveedor.setNombre("ROLE_PROVEEDOR");
        rolProveedor.setDescripcion("Proveedor con permisos de gestión de productos");
        rolProveedor.setPermisos(permisosProveedor);
        rolProveedor.setActivo(true);
        
        rolRepository.saveAll(List.of(rolAdmin, rolUser, rolProveedor));

        // --- 3. Crear los Usuarios y asignarles Roles ---
        Usuario admin = new Usuario();
        admin.setNombres("Administrador");
        admin.setApellidos("Sistema");
        admin.setEmail("admin@jimdur.com");
        admin.setContrasenaHash(passwordEncoder.encode("admin123"));
        admin.setTelefono("999999999");
        admin.setEstadoCuenta("ACTIVO");
        admin.setFechaRegistro(LocalDateTime.now());
        admin.setRoles(Set.of(rolAdmin));

        Usuario user = new Usuario();
        user.setNombres("Cliente");
        user.setApellidos("Ejemplo");
        user.setEmail("cliente@jimdur.com");
        user.setContrasenaHash(passwordEncoder.encode("cliente123"));
        user.setTelefono("888888888");
        user.setEstadoCuenta("ACTIVO");
        user.setFechaRegistro(LocalDateTime.now());
        user.setRoles(Set.of(rolUser));

        Usuario proveedor = new Usuario();
        proveedor.setNombres("Proveedor");
        proveedor.setApellidos("Ejemplo");
        proveedor.setEmail("proveedor@jimdur.com");
        proveedor.setContrasenaHash(passwordEncoder.encode("proveedor123"));
        proveedor.setTelefono("777777777");
        proveedor.setEstadoCuenta("ACTIVO");
        proveedor.setFechaRegistro(LocalDateTime.now());
        proveedor.setRoles(Set.of(rolProveedor));
        
        usuarioRepository.saveAll(List.of(admin, user, proveedor));

        // Poblar la base de negocio con UsuarioRef
        UsuarioRef adminRef = UsuarioRef.builder()
            .id(admin.getId())
            .nombres(admin.getNombres())
            .apellidos(admin.getApellidos())
            .email(admin.getEmail())
            .telefono(admin.getTelefono())
            .estadoCuenta(admin.getEstadoCuenta())
            .fechaRegistro(admin.getFechaRegistro())
            .build();
        UsuarioRef userRef = UsuarioRef.builder()
            .id(user.getId())
            .nombres(user.getNombres())
            .apellidos(user.getApellidos())
            .email(user.getEmail())
            .telefono(user.getTelefono())
            .estadoCuenta(user.getEstadoCuenta())
            .fechaRegistro(user.getFechaRegistro())
            .build();
        UsuarioRef proveedorRef = UsuarioRef.builder()
            .id(proveedor.getId())
            .nombres(proveedor.getNombres())
            .apellidos(proveedor.getApellidos())
            .email(proveedor.getEmail())
            .telefono(proveedor.getTelefono())
            .estadoCuenta(proveedor.getEstadoCuenta())
            .fechaRegistro(proveedor.getFechaRegistro())
            .build();
        usuarioRefRepository.saveAll(List.of(adminRef, userRef, proveedorRef));

        System.out.println("Datos iniciales cargados exitosamente.");
        System.out.println("Usuarios creados:");
        System.out.println("- Admin: admin@jimdur.com / admin123");
        System.out.println("- Cliente: cliente@jimdur.com / cliente123");
        System.out.println("- Proveedor: proveedor@jimdur.com / proveedor123");
    }
} 