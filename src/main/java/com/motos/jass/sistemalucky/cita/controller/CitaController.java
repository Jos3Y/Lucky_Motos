package com.motos.jass.sistemalucky.cita.controller;

import com.motos.jass.sistemalucky.cita.dto.CitaRequestDTO;
import com.motos.jass.sistemalucky.cita.dto.CitaResponseDTO;
import com.motos.jass.sistemalucky.cita.dto.CitaUpdateEstadoDTO;
import com.motos.jass.sistemalucky.cita.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {
    
    private final CitaService citaService;
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'CLIENTE')")
    public ResponseEntity<CitaResponseDTO> crearCita(@RequestBody CitaRequestDTO request) {
        CitaResponseDTO response = citaService.crearCita(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerTodasLasCitas() {
        List<CitaResponseDTO> citas = citaService.obtenerTodasLasCitas();
        return ResponseEntity.ok(citas);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<CitaResponseDTO> obtenerCitaPorId(@PathVariable Long id) {
        CitaResponseDTO cita = citaService.obtenerCitaPorId(id);
        return ResponseEntity.ok(cita);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'CLIENTE')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorCliente(@PathVariable Long clienteId) {
        List<CitaResponseDTO> citas = citaService.obtenerCitasPorCliente(clienteId);
        return ResponseEntity.ok(citas);
    }
    
    @GetMapping("/tecnico/{tecnicoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorTecnico(@PathVariable Long tecnicoId) {
        List<CitaResponseDTO> citas = citaService.obtenerCitasPorTecnico(tecnicoId);
        return ResponseEntity.ok(citas);
    }
    
    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorEstado(@PathVariable String estado) {
        List<CitaResponseDTO> citas = citaService.obtenerCitasPorEstado(estado);
        return ResponseEntity.ok(citas);
    }
    
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<CitaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody CitaUpdateEstadoDTO request) {
        CitaResponseDTO cita = citaService.actualizarEstado(id, request);
        return ResponseEntity.ok(cita);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<CitaResponseDTO> actualizarCita(
            @PathVariable Long id,
            @RequestBody CitaRequestDTO request) {
        CitaResponseDTO cita = citaService.actualizarCita(id, request);
        return ResponseEntity.ok(cita);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCita(@PathVariable Long id) {
        citaService.eliminarCita(id);
        return ResponseEntity.noContent().build();
    }
}

