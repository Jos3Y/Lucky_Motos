package com.motos.jass.sistemalucky.tiposervicio.controller;

import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import com.motos.jass.sistemalucky.tiposervicio.repository.TipoServicioRepository;
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
    
    private final TipoServicioRepository tipoServicioRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<List<TipoServicio>> obtenerTodos() {
        return ResponseEntity.ok(tipoServicioRepository.findAll());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<TipoServicio> obtenerPorId(@PathVariable Long id) {
        return tipoServicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

