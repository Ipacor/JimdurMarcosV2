package com.diedari.jimdur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                // --- 1. Recursos públicos ---
                                                .requestMatchers("/css/**", "/js/**", "/image/**", "/img/**", 
                                                               "/uploads/**", "/webjars/**", "/favicon.ico").permitAll()
                                                .requestMatchers("/", "/auth/**", "/user/login", "/user/register", 
                                                               "/user/productos", "/user/contacto", "/user/nosotros").permitAll()
                                                .requestMatchers("/api/verification/**").permitAll()
                                                
                                                // --- 2. Dashboard y administración general ---
                                                .requestMatchers("/admin", "/admin/").hasAnyAuthority("VER_DASHBOARD", "LEER_PRODUCTOS", "LEER_USUARIOS", "GESTIONAR_ROLES")
                                                
                                                // --- 3. Gestión de Categorías ---
                                                .requestMatchers(HttpMethod.GET, "/admin/categorias", "/admin/categorias/").hasAuthority("LEER_CATEGORIAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/categorias/nueva").hasAuthority("CREAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.POST, "/admin/categorias/guardar").hasAuthority("CREAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/categorias/editar/**").hasAuthority("EDITAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.POST, "/admin/categorias/actualizar/**").hasAuthority("EDITAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/categorias/desactivar/**").hasAuthority("DESACTIVAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/categorias/activar/**").hasAuthority("DESACTIVAR_CATEGORIAS")
                                                
                                                // --- 4. Gestión de Marcas ---
                                                .requestMatchers(HttpMethod.GET, "/admin/marcas", "/admin/marcas/").hasAuthority("LEER_MARCAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/marcas/nueva").hasAuthority("CREAR_MARCAS")
                                                .requestMatchers(HttpMethod.POST, "/admin/marcas/guardar").hasAuthority("CREAR_MARCAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/marcas/editar/**").hasAuthority("EDITAR_MARCAS")
                                                .requestMatchers(HttpMethod.POST, "/admin/marcas/actualizar/**").hasAuthority("EDITAR_MARCAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/marcas/desactivar/**").hasAuthority("DESACTIVAR_MARCAS")
                                                .requestMatchers(HttpMethod.GET, "/admin/marcas/activar/**").hasAuthority("DESACTIVAR_MARCAS")
                                                
                                                // --- 5. Gestión de Productos ---
                                                .requestMatchers(HttpMethod.GET, "/admin/productos", "/admin/productos/").hasAuthority("LEER_PRODUCTOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/productos/nuevo").hasAuthority("CREAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.POST, "/admin/productos/guardar", "/admin/productos/crear").hasAuthority("CREAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/productos/editar/**").hasAuthority("EDITAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.POST, "/admin/productos/actualizar/**").hasAuthority("EDITAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/productos/desactivar/**").hasAuthority("DESACTIVAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/productos/activar/**").hasAuthority("DESACTIVAR_PRODUCTOS")
                                                .requestMatchers("/admin/productos/api/**").hasAuthority("LEER_PRODUCTOS")
                                                
                                                // --- 6. Gestión de Proveedores ---
                                                .requestMatchers(HttpMethod.GET, "/admin/proveedores", "/admin/proveedores/").hasAuthority("LEER_PROVEEDORES")
                                                .requestMatchers(HttpMethod.GET, "/admin/proveedores/nuevo").hasAuthority("CREAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.POST, "/admin/proveedores/guardar").hasAuthority("CREAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.GET, "/admin/proveedores/editar/**").hasAuthority("EDITAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.POST, "/admin/proveedores/actualizar/**").hasAuthority("EDITAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.GET, "/admin/proveedores/desactivar/**").hasAuthority("DESACTIVAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.GET, "/admin/proveedores/activar/**").hasAuthority("DESACTIVAR_PROVEEDORES")
                                                
                                                // --- 7. Gestión de Usuarios ---
                                                .requestMatchers(HttpMethod.GET, "/admin/usuarios", "/admin/usuarios/").hasAuthority("LEER_USUARIOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/usuarios/nuevo").hasAuthority("CREAR_USUARIOS")
                                                .requestMatchers(HttpMethod.POST, "/admin/usuarios/guardar").hasAuthority("CREAR_USUARIOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/usuarios/editar/**").hasAuthority("EDITAR_USUARIOS")
                                                .requestMatchers(HttpMethod.POST, "/admin/usuarios/actualizar/**").hasAuthority("EDITAR_USUARIOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/usuarios/desactivar/**").hasAuthority("DESACTIVAR_USUARIOS")
                                                .requestMatchers(HttpMethod.GET, "/admin/usuarios/activar/**").hasAuthority("DESACTIVAR_USUARIOS")
                                                
                                                // --- 8. Gestión de Ubicaciones ---
                                                .requestMatchers(HttpMethod.GET, "/admin/ubicaciones", "/admin/ubicaciones/").hasAuthority("LEER_UBICACIONES")
                                                .requestMatchers(HttpMethod.GET, "/admin/ubicaciones/nueva").hasAuthority("CREAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.POST, "/admin/ubicaciones/guardar").hasAuthority("CREAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.GET, "/admin/ubicaciones/editar/**").hasAuthority("EDITAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.POST, "/admin/ubicaciones/actualizar/**").hasAuthority("EDITAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.GET, "/admin/ubicaciones/desactivar/**").hasAuthority("DESACTIVAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.GET, "/admin/ubicaciones/activar/**").hasAuthority("DESACTIVAR_UBICACIONES")
                                                
                                                // --- 9. Gestión de Roles y Permisos ---
                                                .requestMatchers("/admin/roles/**").hasAuthority("GESTIONAR_ROLES")
                                                .requestMatchers("/admin/permisos/**").hasAuthority("GESTIONAR_PERMISOS")
                                                
                                                // --- 10. Gestión de Órdenes ---
                                                .requestMatchers(HttpMethod.GET, "/admin/ordenes", "/admin/ordenes/").hasAuthority("LEER_ORDENES")
                                                .requestMatchers(HttpMethod.POST, "/admin/ordenes/aprobar/**").hasAuthority("APROBAR_ORDENES")
                                                .requestMatchers(HttpMethod.POST, "/admin/ordenes/cancelar/**").hasAuthority("CANCELAR_ORDENES")
                                                
                                                // --- 11. APIs REST ---
                                                .requestMatchers(HttpMethod.GET, "/api/categorias/**").hasAuthority("LEER_CATEGORIAS")
                                                .requestMatchers(HttpMethod.POST, "/api/categorias/**").hasAuthority("CREAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.PUT, "/api/categorias/**").hasAuthority("EDITAR_CATEGORIAS")
                                                .requestMatchers(HttpMethod.DELETE, "/api/categorias/**").hasAuthority("DESACTIVAR_CATEGORIAS")
                                                
                                                .requestMatchers(HttpMethod.GET, "/api/marcas/**").hasAuthority("LEER_MARCAS")
                                                .requestMatchers(HttpMethod.POST, "/api/marcas/**").hasAuthority("CREAR_MARCAS")
                                                .requestMatchers(HttpMethod.PUT, "/api/marcas/**").hasAuthority("EDITAR_MARCAS")
                                                .requestMatchers(HttpMethod.DELETE, "/api/marcas/**").hasAuthority("DESACTIVAR_MARCAS")
                                                
                                                .requestMatchers(HttpMethod.GET, "/api/productos/**").hasAuthority("LEER_PRODUCTOS")
                                                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAuthority("CREAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasAuthority("EDITAR_PRODUCTOS")
                                                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasAuthority("DESACTIVAR_PRODUCTOS")
                                                
                                                .requestMatchers(HttpMethod.GET, "/api/proveedores/**").hasAuthority("LEER_PROVEEDORES")
                                                .requestMatchers(HttpMethod.POST, "/api/proveedores/**").hasAuthority("CREAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.PUT, "/api/proveedores/**").hasAuthority("EDITAR_PROVEEDORES")
                                                .requestMatchers(HttpMethod.DELETE, "/api/proveedores/**").hasAuthority("DESACTIVAR_PROVEEDORES")
                                                
                                                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAuthority("LEER_USUARIOS")
                                                .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasAuthority("CREAR_USUARIOS")
                                                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAuthority("EDITAR_USUARIOS")
                                                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("DESACTIVAR_USUARIOS")
                                                
                                                .requestMatchers(HttpMethod.GET, "/api/ubicaciones/**").hasAuthority("LEER_UBICACIONES")
                                                .requestMatchers(HttpMethod.POST, "/api/ubicaciones/**").hasAuthority("CREAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.PUT, "/api/ubicaciones/**").hasAuthority("EDITAR_UBICACIONES")
                                                .requestMatchers(HttpMethod.DELETE, "/api/ubicaciones/**").hasAuthority("DESACTIVAR_UBICACIONES")
                                                
                                                // --- 12. Otras rutas protegidas ---
                                                .anyRequest().authenticated()
                                )
                                .formLogin(form -> form
                                                .loginPage("/user/login")
                                                .loginProcessingUrl("/user/login")
                                                .defaultSuccessUrl("/admin", true)
                                                .failureUrl("/user/login?error=true")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutUrl("/user/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll())
                                .exceptionHandling(ex -> ex
                                                .accessDeniedPage("/acceso-denegado"))
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/api/**", "/admin/productos/api/**"));

                return http.build();
        }
}
