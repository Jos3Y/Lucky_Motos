package com.motos.jass.sistemalucky.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminLoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Crear o obtener rol ADMIN
        Rol adminRole = rolRepository.findByDescripcion("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol role = new Rol();
                    role.setDescripcion("ROLE_ADMIN");
                    return rolRepository.save(role);
                });

        // Crear o actualizar usuario admin
        Socio admin = socioRepository.findByCorreo("admin@example.com")
                .orElseGet(() -> {
                    Socio newAdmin = new Socio();
                    newAdmin.setNombre("Admin");
                    newAdmin.setApellidos("Sistema");
                    newAdmin.setCorreo("admin@example.com");
                    newAdmin.setContrasena(passwordEncoder.encode("admin123"));
                    newAdmin.setEstado(true);
                    newAdmin.setRolSocios(new ArrayList<>());
                    return socioRepository.save(newAdmin);
                });

        // Asegurar que tenga el rol ADMIN
        boolean hasAdminRole = admin.getRolSocios().stream()
                .anyMatch(rs -> rs.getRol().getDescripcion().equals("ROLE_ADMIN") && rs.getEstado().equals("ACTIVO"));

        if (!hasAdminRole) {
            RolSocio rs = new RolSocio();
            rs.setRol(adminRole);
            rs.setSocio(admin);
            rs.setDescripcion("Administrador del sistema");
            rs.setEstado("ACTIVO");
            admin.getRolSocios().add(rs);
            socioRepository.save(admin);
        }
    }

    @Test
    public void adminLoginShouldReturnToken() {
        // Verificar que el admin existe en la BD
        Socio admin = socioRepository.findByCorreo("admin@example.com")
                .orElse(null);
        
        assertThat(admin).isNotNull();
        assertThat(admin.getCorreo()).isEqualTo("admin@example.com");
        assertThat(admin.getEstado()).isTrue();

        // Realizar login
        String url = "http://localhost:" + port + "/auth/login";
        String body = "{\"correo\":\"admin@example.com\",\"contrasena\":\"admin123\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Verificar respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).contains("token");
        assertThat(response.getBody()).contains("admin@example.com");

        // Verificar que el token es válido parseando la respuesta
        try {
            Map<String, Object> loginData = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<Map<String, Object>>() {}
            );
            
            assertThat(loginData).containsKey("token");
            assertThat(loginData.get("token")).isNotNull();
            assertThat(loginData.get("correo")).isEqualTo("admin@example.com");
            assertThat(loginData.get("roles")).isNotNull();
            
            System.out.println("✅ Login exitoso para admin@example.com");
            System.out.println("Token: " + ((String) loginData.get("token")).substring(0, 50) + "...");
            System.out.println("Roles: " + loginData.get("roles"));
            
        } catch (Exception e) {
            throw new AssertionError("Error parseando respuesta de login: " + e.getMessage());
        }
    }

    @Test
    public void adminLoginWithWrongPasswordShouldFail() {
        String url = "http://localhost:" + port + "/auth/login";
        String body = "{\"correo\":\"admin@example.com\",\"contrasena\":\"wrongpassword\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // Debe fallar con contraseña incorrecta
        assertThat(response.getStatusCode()).isIn(HttpStatus.UNAUTHORIZED, HttpStatus.BAD_REQUEST);
    }
}

