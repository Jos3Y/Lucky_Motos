package com.motos.jass.sistemalucky.tiposervicio.controller;

import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioRequestDTO;
import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioResponseDTO;
import com.motos.jass.sistemalucky.tiposervicio.service.TipoServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/tipos-servicio")
@RequiredArgsConstructor
public class TipoServicioController {
    
    private final TipoServicioService tipoServicioService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<TipoServicioResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(tipoServicioService.listarTodos());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<TipoServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoServicioService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TipoServicioResponseDTO> crear(@RequestBody TipoServicioRequestDTO request) {
        return ResponseEntity.ok(tipoServicioService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<TipoServicioResponseDTO> actualizar(@PathVariable Long id,
                                                              @RequestBody TipoServicioRequestDTO request) {
        return ResponseEntity.ok(tipoServicioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoServicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

