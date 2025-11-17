package com.motos.jass.sistemalucky.reserva.controller;


import com.motos.jass.sistemalucky.auth.jwt.JwtUtil;
import com.motos.jass.sistemalucky.reserva.dto.ReservaRequestDTO;
import com.motos.jass.sistemalucky.reserva.dto.ReservaResponseDTO;
import com.motos.jass.sistemalucky.reserva.service.ReservaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaServiceImpl reservaServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    // 🔹 Crear reserva
    @PostMapping("/save")
    public ResponseEntity<ReservaResponseDTO> crearReserva(
            @RequestBody ReservaRequestDTO request,
            @RequestHeader("Authorization") String tokenHeader) {

        try {
            // 🧩 Extraer el token limpio (sin "Bearer ")
            String token = tokenHeader.replace("Bearer ", "");

            // 🔥 Llamar al servicio que maneja la lógica
            ReservaResponseDTO response = reservaServiceImpl.crearReserva(request, token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
