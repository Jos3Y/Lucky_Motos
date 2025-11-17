package com.motos.jass.sistemalucky.moto.controller;


import com.motos.jass.sistemalucky.moto.dto.MotoRequestDTO;
import com.motos.jass.sistemalucky.moto.dto.MotoResponseDTO;
import com.motos.jass.sistemalucky.moto.service.MotoService;
import com.motos.jass.sistemalucky.moto.service.MotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/motos")
public class MotoController {

    @Autowired
    private MotoService service;;

    @Autowired
    private MotoServiceImpl motoServiceImpl;


    @PostMapping("/save")
    public ResponseEntity<MotoResponseDTO> crearMoto(@RequestBody MotoRequestDTO request) {
        System.out.println("Entrando a la peticion:"+request);
        MotoResponseDTO MotoResponse = motoServiceImpl.crearMoto(request);
        return ResponseEntity.status(201).body(MotoResponse );
    }

    // 🔹 Actualizar moto
    @PutMapping("/update/{id}")
    public ResponseEntity<MotoResponseDTO> actualizarMoto(@PathVariable Long id, @RequestBody MotoRequestDTO request) {
        MotoResponseDTO motoActualizada = motoServiceImpl.actualizarMoto(id, request);
        return ResponseEntity.ok(motoActualizada);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO', 'CLIENTE', 'SOCIO')")
    public ResponseEntity<List<MotoResponseDTO>> listarMotos() {
        return ResponseEntity.ok(motoServiceImpl.listarMotos());
    }

    @PutMapping("/eliminar/{id}")
    public ResponseEntity<MotoResponseDTO> eliminarMoto(@PathVariable Long id) {
        MotoResponseDTO moto = motoServiceImpl.eliminarMoto(id);
        return ResponseEntity.ok(moto);
    }


    //LISTA MOTOS ACTIVAS
    @GetMapping("/activas")
    public ResponseEntity<List<MotoResponseDTO>> listarActivas() {
        List<MotoResponseDTO> motos = motoServiceImpl.listarActivas();
        return ResponseEntity.ok(motos);
    }

    // OBTENER MODELOS ÚNICOS
    @GetMapping("/modelos")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<String>> obtenerModelosUnicos() {
        List<String> modelos = motoServiceImpl.obtenerModelosUnicos();
        return ResponseEntity.ok(modelos);
    }

    // OBTENER DATOS DE MOTO POR MODELO
    @GetMapping("/modelo/{modelo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<MotoResponseDTO> obtenerMotoPorModelo(@PathVariable String modelo) {
        MotoResponseDTO moto = motoServiceImpl.obtenerMotoPorModelo(modelo);
        return ResponseEntity.ok(moto);
    }





}
