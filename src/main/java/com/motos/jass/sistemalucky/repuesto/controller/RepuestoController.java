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
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<RepuestoResponseDTO> crearRepuesto(@RequestBody RepuestoRequestDTO request) {
        RepuestoResponseDTO response = repuestoService.crearRepuesto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<RepuestoResponseDTO>> obtenerTodosLosRepuestos(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String repuesto) {
        
        if (marca != null || modelo != null || repuesto != null) {
            RepuestoFilterDTO filter = new RepuestoFilterDTO();
            filter.setMarca(marca);
            filter.setModelo(modelo);
            filter.setRepuesto(repuesto);
            List<RepuestoResponseDTO> repuestos = repuestoService.filtrarRepuestos(filter);
            return ResponseEntity.ok(repuestos);
        }
        
        List<RepuestoResponseDTO> repuestos = repuestoService.obtenerTodosLosRepuestos();
        return ResponseEntity.ok(repuestos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<RepuestoResponseDTO> obtenerRepuestoPorId(@PathVariable Long id) {
        RepuestoResponseDTO repuesto = repuestoService.obtenerRepuestoPorId(id);
        return ResponseEntity.ok(repuesto);
    }
    
    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<RepuestoResponseDTO>> obtenerRepuestosBajoStock() {
        List<RepuestoResponseDTO> repuestos = repuestoService.obtenerRepuestosBajoStock();
        return ResponseEntity.ok(repuestos);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<RepuestoResponseDTO> actualizarRepuesto(
            @PathVariable Long id,
            @RequestBody RepuestoRequestDTO request) {
        RepuestoResponseDTO repuesto = repuestoService.actualizarRepuesto(id, request);
        return ResponseEntity.ok(repuesto);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarRepuesto(@PathVariable Long id) {
        repuestoService.eliminarRepuesto(id);
        return ResponseEntity.noContent().build();
    }
}

