package com.motos.jass.sistemalucky.socio.controller;

import com.motos.jass.sistemalucky.socio.dto.SocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.SocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.service.SocioService;
import com.motos.jass.sistemalucky.socio.service.SocioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/socio")
public class SocioController {

    private final SocioService socioService;
    private final SocioServiceImpl socioServiceImpl;

    @PostMapping("/registro")
    public Socio registrar(@RequestBody Socio socio) {

        return socioService.save(socio); // Guarda y devuelve JSON
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SocioResponseDTO> guardarSocio(@RequestBody SocioRequestDTO requestDTO) {
        SocioResponseDTO responseEntity = socioServiceImpl.savePartner(requestDTO);
        return ResponseEntity.status(201).body(responseEntity);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SocioResponseDTO>> listar() {
        List<SocioResponseDTO> socios = socioServiceImpl.listarSociosDTO();
        return ResponseEntity.ok(socios);

    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SocioResponseDTO> actualizarSocio(@PathVariable Long id, @RequestBody SocioRequestDTO request) {
        SocioResponseDTO socioActualizado = socioServiceImpl.actualizarSocio(id, request);
        return ResponseEntity.ok(socioActualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        socioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
