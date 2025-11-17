package com.motos.jass.sistemalucky.socio.controller;

import com.motos.jass.sistemalucky.socio.dto.RolRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolResponseDTO;
import com.motos.jass.sistemalucky.socio.service.RolServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rol")
public class RolController {

    private RolResponseDTO rolResponseDTO;
    private final RolServiceImpl rolServiceImpl;

    @PostMapping("/registro")
    public ResponseEntity<RolResponseDTO> crearRol(@RequestBody RolRequestDTO dto) {
        System.out.println("Entrando a la peticion"+dto);
        RolResponseDTO rolResponse  = rolServiceImpl.crearRol(dto);
        return ResponseEntity.status(201).body(rolResponse );
    }



}
