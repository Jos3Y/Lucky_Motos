package com.motos.jass.sistemalucky.tecnico.controller;

import com.motos.jass.sistemalucky.tecnico.dto.DisponibilidadRequestDTO;
import com.motos.jass.sistemalucky.tecnico.dto.TecnicoRequestDTO;
import com.motos.jass.sistemalucky.tecnico.dto.TecnicoResponseDTO;
import com.motos.jass.sistemalucky.tecnico.service.TecnicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tecnicos")
@RequiredArgsConstructor
public class TecnicoController {
    
    private final TecnicoService tecnicoService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<List<TecnicoResponseDTO>> obtenerTodosLosTecnicos() {
        List<TecnicoResponseDTO> tecnicos = tecnicoService.obtenerTodosLosTecnicos();
        return ResponseEntity.ok(tecnicos);
    }
    
    @GetMapping("/disponibles")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<List<TecnicoResponseDTO>> obtenerTecnicosDisponibles() {
        List<TecnicoResponseDTO> tecnicos = tecnicoService.obtenerTecnicosDisponibles();
        return ResponseEntity.ok(tecnicos);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<TecnicoResponseDTO> obtenerTecnicoPorId(@PathVariable Long id) {
        TecnicoResponseDTO tecnico = tecnicoService.obtenerTecnicoPorId(id);
        return ResponseEntity.ok(tecnico);
    }
    
    @GetMapping("/socio/{socioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<TecnicoResponseDTO> obtenerTecnicoPorSocioId(@PathVariable Long socioId) {
        TecnicoResponseDTO tecnico = tecnicoService.obtenerTecnicoPorSocioId(socioId);
        return ResponseEntity.ok(tecnico);
    }
    
    @PutMapping("/{id}/disponibilidad")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<Void> actualizarDisponibilidad(
            @PathVariable Long id,
            @RequestBody DisponibilidadRequestDTO request) {
        tecnicoService.actualizarDisponibilidad(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TecnicoResponseDTO> crearTecnico(@RequestBody TecnicoRequestDTO request) {
        return ResponseEntity.ok(tecnicoService.crearTecnico(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TecnicoResponseDTO> actualizarTecnico(
            @PathVariable Long id,
            @RequestBody TecnicoRequestDTO request) {
        return ResponseEntity.ok(tecnicoService.actualizarTecnico(id, request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        tecnicoService.cambiarEstado(id, estado);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarTecnico(@PathVariable Long id) {
        tecnicoService.eliminarTecnico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reset-horario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetearHorarios() {
        tecnicoService.resetearHorariosDefault();
        return ResponseEntity.ok().build();
    }
}

