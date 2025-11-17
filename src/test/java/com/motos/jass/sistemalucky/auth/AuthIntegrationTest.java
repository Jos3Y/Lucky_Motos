package com.motos.jass.sistemalucky.auth;

import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private RolRepository rolRepository;

    private Socio testUser;
    private Rol testRole;

    @BeforeEach
    public void setUp() {
        // Ensure a role exists
        testRole = rolRepository.findById(999L).orElse(null);
        if (testRole == null) {
            testRole = new Rol();
            testRole.setDescripcion("TEST_ROLE");
            rolRepository.save(testRole);
        }

        // Create test user
        testUser = new Socio();
        testUser.setNombre("Test");
        testUser.setApellidos("User");
        testUser.setCorreo("testuser@example.com");
        testUser.setContrasena(passwordEncoder.encode("admin123"));
        testUser.setEstado(true);
        // assign role via RolSocio
        RolSocio rs = new RolSocio();
        rs.setRol(testRole);
        rs.setSocio(testUser);
        rs.setDescripcion("Test role");
        rs.setEstado("ACTIVO");
        testUser.setRolSocios(new ArrayList<>());
        testUser.getRolSocios().add(rs);

        socioRepository.save(testUser);
    }

    @AfterEach
    public void tearDown() {
        if (testUser != null) {
            try {
                socioRepository.deleteById(testUser.getId());
            } catch (Exception ignored) {
            }
        }
        // Note: leave role to avoid interference
    }

    @Test
    public void loginShouldReturnToken() {
        String url = "http://localhost:" + port + "/auth/login";

        String body = "{\"correo\":\"testuser@example.com\",\"contrasena\":\"admin123\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("token");
        assertThat(response.getBody()).contains("testuser@example.com");
    }

}
