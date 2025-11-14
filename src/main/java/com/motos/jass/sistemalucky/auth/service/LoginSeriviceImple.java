package com.motos.jass.sistemalucky.auth.service;

import com.motos.jass.sistemalucky.auth.dto.LoginResponseDTO;
import com.motos.jass.sistemalucky.auth.jwt.JwtUtil;
import com.motos.jass.sistemalucky.auth.mapper.LoginMapper;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor

@Service
public class LoginSeriviceImple {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;
    private final SocioRepository socioRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponseDTO login(String email, String password) {
        Socio socio = socioRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Correo no registrado"));

        // Debug: imprimir información
        System.out.println("=== DEBUG LOGIN ===");
        System.out.println("Email: " + email);
        System.out.println("Password recibida: " + password);
        System.out.println("Hash en BD: " + (socio.getContrasena() != null
                ? socio.getContrasena().substring(0, Math.min(30, socio.getContrasena().length()))
                : "NULL"));
        System.out.println("Password matches: " + passwordEncoder.matches(password, socio.getContrasena()));

        if (socio.getContrasena() == null || socio.getContrasena().isEmpty()) {
            throw new RuntimeException("Usuario sin contraseña asignada");
        }

        if (!passwordEncoder.matches(password, socio.getContrasena())) {
            System.out.println("ERROR: Contraseña no coincide");
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Verificar que tenga roles
        if (socio.getRolSocios() == null || socio.getRolSocios().isEmpty()) {
            System.out.println("ERROR: Usuario sin roles asignados");
            throw new RuntimeException("Usuario sin roles asignados. Contacte al administrador.");
        }

        LoginResponseDTO response = loginMapper.toResponseDTO(socio);

        // Verificar que tenga roles en la respuesta
        if (response.getRoles() == null || response.getRoles().isEmpty()) {
            System.out.println("ERROR: Usuario sin roles activos");
            throw new RuntimeException("Usuario sin roles activos. Contacte al administrador.");
        }

        System.out.println("Socio encontrado: id=" + socio.getId());
        System.out.println("Roles del usuario: " + response.getRoles());
        String token = jwtUtil.generateToken(response.getCorreo(), response.getRoles());
        response.setToken(token);
        System.out.println("Token generado: " + token.substring(0, Math.min(50, token.length())) + "...");

        return response;
    }

}
