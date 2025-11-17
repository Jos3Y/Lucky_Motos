package com.motos.jass.sistemalucky.repuesto.controller;

import com.motos.jass.sistemalucky.repuesto.dto.RepuestoFilterDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoRequestDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoResponseDTO;
import com.motos.jass.sistemalucky.repuesto.service.RepuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
public class RepuestoController {
    
    private final RepuestoService repuestoService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<RepuestoResponseDTO> crearRepuesto(@RequestBody RepuestoRequestDTO request) {
        RepuestoResponseDTO response = repuestoService.crearRepuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<List<RepuestoResponseDTO>> obtenerTodosLosRepuestos(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String repuesto) {
        
        // Log para depuración
        System.out.println("DEBUG - Parámetros recibidos: marca='" + marca + "', modelo='" + modelo + "', repuesto='" + repuesto + "'");
        
        // Normalizar strings vacíos a null
        marca = (marca != null && marca.trim().isEmpty()) ? null : marca;
        modelo = (modelo != null && modelo.trim().isEmpty()) ? null : modelo;
        repuesto = (repuesto != null && repuesto.trim().isEmpty()) ? null : repuesto;
        
        System.out.println("DEBUG - Parámetros normalizados: marca=" + marca + ", modelo=" + modelo + ", repuesto=" + repuesto);
        
        if (marca != null || modelo != null || repuesto != null) {
            System.out.println("DEBUG - Usando filtros");
            RepuestoFilterDTO filter = new RepuestoFilterDTO();
            filter.setMarca(marca);
            filter.setModelo(modelo);
            filter.setRepuesto(repuesto);
            List<RepuestoResponseDTO> repuestos = repuestoService.filtrarRepuestos(filter);
            System.out.println("DEBUG - Repuestos encontrados con filtros: " + repuestos.size());
            return ResponseEntity.ok(repuestos);
        }
        
        System.out.println("DEBUG - Obteniendo todos los repuestos sin filtros");
        List<RepuestoResponseDTO> repuestos = repuestoService.obtenerTodosLosRepuestos();
        System.out.println("DEBUG - Total de repuestos: " + repuestos.size());
        return ResponseEntity.ok(repuestos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<RepuestoResponseDTO> obtenerRepuestoPorId(@PathVariable Long id) {
        RepuestoResponseDTO repuesto = repuestoService.obtenerRepuestoPorId(id);
        return ResponseEntity.ok(repuesto);
    }
    
    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<List<RepuestoResponseDTO>> obtenerRepuestosBajoStock() {
        List<RepuestoResponseDTO> repuestos = repuestoService.obtenerRepuestosBajoStock();
        return ResponseEntity.ok(repuestos);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<RepuestoResponseDTO> actualizarRepuesto(
            @PathVariable Long id,
            @RequestBody RepuestoRequestDTO request) {
        RepuestoResponseDTO repuesto = repuestoService.actualizarRepuesto(id, request);
        return ResponseEntity.ok(repuesto);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<Void> eliminarRepuesto(@PathVariable Long id) {
        repuestoService.eliminarRepuesto(id);
        return ResponseEntity.noContent().build();
    }
}

