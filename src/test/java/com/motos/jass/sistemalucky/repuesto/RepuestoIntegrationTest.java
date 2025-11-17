package com.motos.jass.sistemalucky.repuesto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoResponseDTO;
import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import com.motos.jass.sistemalucky.repuesto.repository.RepuestoRepository;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepuestoIntegrationTest {

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

    @Autowired
    private RepuestoRepository repuestoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Socio testUser;
    private Rol testRole;
    private String authToken;

    @BeforeEach
    public void setUp() {
        // Crear o reutilizar rol ADMIN
        testRole = rolRepository.findByDescripcion("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol role = new Rol();
                    role.setDescripcion("ROLE_ADMIN");
                    return rolRepository.save(role);
                });

        // Crear usuario de prueba con rol ADMIN
        testUser = new Socio();
        testUser.setNombre("Test");
        testUser.setApellidos("Admin");
        testUser.setCorreo("testadmin@example.com");
        testUser.setContrasena(passwordEncoder.encode("admin123"));
        testUser.setEstado(true);

        RolSocio rs = new RolSocio();
        rs.setRol(testRole);
        rs.setSocio(testUser);
        rs.setDescripcion("Test admin role");
        rs.setEstado("ACTIVO");
        testUser.setRolSocios(new ArrayList<>());
        testUser.getRolSocios().add(rs);

        socioRepository.save(testUser);

        // Autenticarse y obtener token
        String loginUrl = "http://localhost:" + port + "/auth/login";
        String loginBody = "{\"correo\":\"testadmin@example.com\",\"contrasena\":\"admin123\"}";

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, loginHeaders);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

        if (loginResponse.getStatusCode() == HttpStatus.OK && loginResponse.getBody() != null) {
            try {
                Map<String, Object> loginData = objectMapper.readValue(
                        loginResponse.getBody(),
                        new TypeReference<Map<String, Object>>() {}
                );
                authToken = (String) loginData.get("token");
            } catch (Exception e) {
                System.err.println("Error parsing login response: " + e.getMessage());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (testUser != null) {
            try {
                Long userId = testUser.getId();
                if (userId != null) {
                    socioRepository.deleteById(userId);
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Test
    public void testObtenerTodosLosRepuestos() throws Exception {
        // Verificar que hay repuestos en la BD
        long totalRepuestos = repuestoRepository.count();
        System.out.println("DEBUG - Total de repuestos en BD: " + totalRepuestos);

        // Preparar headers con token de autenticación
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Hacer petición GET sin parámetros
        String url = "http://localhost:" + port + "/api/repuestos";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        System.out.println("DEBUG - Status Code: " + response.getStatusCode());
        System.out.println("DEBUG - Response Body: " + response.getBody());

        // Verificar respuesta
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Parsear respuesta
        List<RepuestoResponseDTO> repuestos = objectMapper.readValue(
                response.getBody(),
                new TypeReference<List<RepuestoResponseDTO>>() {}
        );

        System.out.println("DEBUG - Cantidad de repuestos recibidos: " + repuestos.size());

        // Verificar que se recibieron repuestos
        assertThat(repuestos).isNotNull();
        assertThat(repuestos.size()).isGreaterThanOrEqualTo(0);

        // Si hay repuestos en la BD, verificar que se recibieron
        if (totalRepuestos > 0) {
            assertThat(repuestos.size()).isEqualTo((int) totalRepuestos);

            // Verificar estructura de al menos un repuesto
            RepuestoResponseDTO primerRepuesto = repuestos.get(0);
            assertThat(primerRepuesto.getId()).isNotNull();
            assertThat(primerRepuesto.getNombre()).isNotNull();
            assertThat(primerRepuesto.getStock()).isNotNull();
            assertThat(primerRepuesto.getPrecio()).isNotNull();
            assertThat(primerRepuesto.getEstado()).isNotNull();
        }
    }

    @Test
    public void testObtenerRepuestosSinFiltros() throws Exception {
        // Preparar headers con token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Hacer petición GET sin query parameters
        String url = "http://localhost:" + port + "/api/repuestos";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        System.out.println("DEBUG - Test sin filtros - Status: " + response.getStatusCode());
        System.out.println("DEBUG - Test sin filtros - Body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testObtenerRepuestosConFiltroMarca() throws Exception {
        // Obtener una marca existente de la BD
        List<Repuesto> repuestosBD = repuestoRepository.findAll();
        if (repuestosBD.isEmpty()) {
            System.out.println("DEBUG - No hay repuestos en BD para probar filtro");
            return;
        }

        String marcaExistente = repuestosBD.stream()
                .filter(r -> r.getMarca() != null && !r.getMarca().isEmpty())
                .map(Repuesto::getMarca)
                .findFirst()
                .orElse(null);

        if (marcaExistente == null) {
            System.out.println("DEBUG - No hay marcas disponibles para probar filtro");
            return;
        }

        // Preparar headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Hacer petición GET con filtro de marca
        String url = "http://localhost:" + port + "/api/repuestos?marca=" + marcaExistente;
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        System.out.println("DEBUG - Test con filtro marca - Status: " + response.getStatusCode());
        System.out.println("DEBUG - Test con filtro marca - Body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        if (response.getBody() != null) {
            List<RepuestoResponseDTO> repuestos = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<RepuestoResponseDTO>>() {}
            );

            // Verificar que todos los repuestos tienen la marca filtrada
            repuestos.forEach(r -> {
                assertThat(r.getMarca()).isEqualTo(marcaExistente);
            });
        }
    }

    @Test
    public void testObtenerRepuestosConParametrosVacios() throws Exception {
        // Preparar headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (authToken != null) {
            headers.setBearerAuth(authToken);
        }

        HttpEntity<String> request = new HttpEntity<>(headers);

        // Hacer petición GET con parámetros vacíos (simulando el problema original)
        String url = "http://localhost:" + port + "/api/repuestos?marca=&modelo=&repuesto=";
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                String.class
        );

        System.out.println("DEBUG - Test con parámetros vacíos - Status: " + response.getStatusCode());
        System.out.println("DEBUG - Test con parámetros vacíos - Body: " + response.getBody());

        // Debe devolver todos los repuestos (no filtrar)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        if (response.getBody() != null) {
            List<RepuestoResponseDTO> repuestos = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<RepuestoResponseDTO>>() {}
            );

            long totalBD = repuestoRepository.count();
            System.out.println("DEBUG - Total en BD: " + totalBD + ", Total recibido: " + repuestos.size());

            // Si hay repuestos en BD, debe devolverlos todos
            if (totalBD > 0) {
                assertThat(repuestos.size()).isEqualTo((int) totalBD);
            }
        }
    }
}

