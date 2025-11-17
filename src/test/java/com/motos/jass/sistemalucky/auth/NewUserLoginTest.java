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
public class NewUserLoginTest {

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

    private Socio createdUser;
    private Rol createdRole;

    @BeforeEach
    public void setUp() {
        // create or reuse a role
        createdRole = rolRepository.findAll().stream().findFirst().orElse(null);
        if (createdRole == null) {
            createdRole = new Rol();
            createdRole.setDescripcion("AUTO_ROLE");
            rolRepository.save(createdRole);
        }

        // create user
        createdUser = new Socio();
        createdUser.setNombre("Nuevo");
        createdUser.setApellidos("Usuario");
        createdUser.setCorreo("nuevo@example.com");
        createdUser.setContrasena(passwordEncoder.encode("admin123"));
        createdUser.setEstado(true);

        RolSocio rs = new RolSocio();
        rs.setRol(createdRole);
        rs.setSocio(createdUser);
        rs.setDescripcion("Auto role");
        rs.setEstado("ACTIVO");
        createdUser.setRolSocios(new ArrayList<>());
        createdUser.getRolSocios().add(rs);

        socioRepository.save(createdUser);
    }

    @AfterEach
    public void tearDown() {
        try {
            if (createdUser != null && createdUser.getId() > 0) {
                socioRepository.deleteById(createdUser.getId());
            }
        } catch (Exception ignored) {
        }
    }

    @Test
    public void newUserLoginShouldReturnToken() {
        String url = "http://localhost:" + port + "/auth/login";

        String body = "{\"correo\":\"nuevo@example.com\",\"contrasena\":\"admin123\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("token");
        assertThat(response.getBody()).contains("nuevo@example.com");
    }
}
