package com.diedari.jimdur.controller;

import com.diedari.jimdur.service.DataMigrationService;
import com.diedari.jimdur.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para manejar la migración de datos entre bases de datos.
 * Solo debe usarse durante la migración inicial.
 */
@RestController
@RequestMapping("/api/migration")
public class MigrationController {

    @Autowired
    private DataMigrationService dataMigrationService;

    @Autowired
    private SyncService syncService;

    /**
     * Endpoint para migrar todos los usuarios existentes.
     */
    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, Object>> migrarUsuarios() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            dataMigrationService.migrarUsuariosExistentes();
            response.put("success", true);
            response.put("message", "Migración de usuarios completada");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en la migración: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Endpoint para verificar la consistencia de datos.
     */
    @GetMapping("/consistencia")
    public ResponseEntity<Map<String, Object>> verificarConsistencia() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            dataMigrationService.verificarConsistencia();
            response.put("success", true);
            response.put("message", "Verificación de consistencia completada");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en la verificación: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * Endpoint para sincronizar un usuario específico.
     */
    @PostMapping("/sincronizar/{usuarioId}")
    public ResponseEntity<Map<String, Object>> sincronizarUsuario(@PathVariable Long usuarioId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            syncService.sincronizarUsuario(usuarioId);
            response.put("success", true);
            response.put("message", "Usuario sincronizado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sincronizando usuario: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 