package com.motos.jass.sistemalucky.auth.controller;

import com.motos.jass.sistemalucky.auth.dto.LoginRequestDTO;
import com.motos.jass.sistemalucky.auth.dto.LoginResponseDTO;
import com.motos.jass.sistemalucky.auth.dto.RegisterRequestDTO;
import com.motos.jass.sistemalucky.auth.dto.RegisterResponseDTO;
import com.motos.jass.sistemalucky.auth.service.LoginSeriviceImple;
import com.motos.jass.sistemalucky.auth.service.RegisterServiceImple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RegisterServiceImple registerServiceImple;
    private final LoginSeriviceImple loginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = loginService.login(loginRequest.getCorreo(), loginRequest.getContrasena());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    // Clase interna para respuestas de error
    private static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registrar(@RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = registerServiceImple.registrarNuevoSocio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
