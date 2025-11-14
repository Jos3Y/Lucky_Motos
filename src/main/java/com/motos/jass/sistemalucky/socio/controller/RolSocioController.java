package com.motos.jass.sistemalucky.socio.controller;

import com.motos.jass.sistemalucky.socio.dto.RolSocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolSocioResponseDTO;
import com.motos.jass.sistemalucky.socio.service.RolSocioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rol-socio")
@RequiredArgsConstructor
public class RolSocioController {


    private final RolSocioServiceImpl rolSocioServiceImpl;

    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/asignar")
    public ResponseEntity<RolSocioResponseDTO> asignarRol(@RequestBody RolSocioRequestDTO request) {
        RolSocioResponseDTO response = rolSocioServiceImpl.asignarRol(request);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('SOCIO')")
    @PostMapping("/quitar")
    public ResponseEntity<RolSocioResponseDTO> quitarRol(@RequestBody RolSocioRequestDTO request) {
        RolSocioResponseDTO response = rolSocioServiceImpl.quitarRol(request);
        return ResponseEntity.ok(response);}


}
