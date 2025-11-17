package com.motos.jass.sistemalucky.socio.service;

import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
import com.motos.jass.sistemalucky.socio.dto.RolSocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.SocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.SocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.mapper.SocioMapper;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import com.motos.jass.sistemalucky.socio.repository.RolSocioRepository;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocioServiceImpl extends BaseServiceImpl<Socio, Long> implements SocioService {

    private final PasswordEncoder passwordEncoder;
    private final SocioMapper socioMapper;
    private final RolSocioService rolSocioService;
    private final RolRepository rolRepository;
    private final RolSocioRepository rolSocioRepository;

    public SocioServiceImpl(SocioRepository repository, PasswordEncoder passwordEncoder, SocioMapper socioMapper,
            RolSocioService rolSocioService, RolRepository rolRepository,
            RolSocioRepository rolSocioRepository) {

        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.socioMapper = socioMapper;

        this.rolSocioService = rolSocioService;
        this.rolRepository = rolRepository;
        this.rolSocioRepository = rolSocioRepository;
    }

    @Override
    public Socio save(Socio socio) {
        // 🔹 Hashear la contraseña antes de guardar
        socio.setContrasena(passwordEncoder.encode(socio.getContrasena()));
        return super.save(socio); // Usa el método de la clase padre
    }

    @Transactional
    public SocioResponseDTO savePartner(SocioRequestDTO request) {

        Socio socio = socioMapper.toEntity(request);
        Socio savedSocio = repository.save(socio);

        asignarRolSiCorresponde(savedSocio.getCorreo(), request.getRol());

        SocioResponseDTO responseDTO = socioMapper.toResponseDTO(savedSocio);
        responseDTO.setRol(obtenerRolActivo(savedSocio));
        return responseDTO;
    }

    // lista solo la información del DTO
    public List<SocioResponseDTO> listarSociosDTO() {
        return repository.findAll().stream()
                .map(socio -> {
                    SocioResponseDTO dto = new SocioResponseDTO();
                    dto.setIdSocio(socio.getId());
                    dto.setNombres(socio.getNombre());
                    dto.setApellidos(socio.getApellidos());
                    dto.setDni(socio.getDni());
                    dto.setCorreo(socio.getCorreo());
                    dto.setTelefono(socio.getTelefono());
                    dto.setGenero(socio.getGenero());
                    dto.setFechaNacimiento(socio.getFechaNacimiento());
                    dto.setEstadoCivil(socio.getEstadoCivil());
                    dto.setRol(obtenerRolActivo(socio));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public SocioResponseDTO actualizarSocio(Long id, SocioRequestDTO request) {
        System.out.println("Entrando a actualizar socio ID: " + id);

        // 1 Buscar el socio existente
        Socio socioExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + id));

        // 2 Actualizar manually los campos editables
        socioExistente.setNombre(request.getNombre());
        socioExistente.setApellidos(request.getApellidos());
        socioExistente.setDni(request.getDni());
        socioExistente.setCorreo(request.getCorreo());
        socioExistente.setTelefono(request.getTelefono());
        socioExistente.setGenero(request.getGenero());
        socioExistente.setFechaNacimiento(request.getFechaNacimiento());
        socioExistente.setEstadoCivil(request.getEstadoCivil());
        // Hashear la contraseña si viene en la petición
        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            socioExistente.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        // 3 Guardar los cambios
        repository.save(socioExistente);

        // 3.1 Actualizar rol si es necesario
        actualizarRolSiCorresponde(socioExistente, request.getRol());

        // 4 Retornar en formato DTO
        SocioResponseDTO responseDTO = socioMapper.toResponseDTO(socioExistente);
        responseDTO.setRol(obtenerRolActivo(socioExistente));
        return responseDTO;
    }

    private void asignarRolSiCorresponde(String correoSocio, String rol) {
        if (rol == null || rol.isBlank()) {
            return;
        }
        RolSocioRequestDTO requestDTO = new RolSocioRequestDTO();
        requestDTO.setCorreoSocio(correoSocio);
        requestDTO.setNombreRol(rol.toUpperCase().startsWith("ROLE_") ? rol.toUpperCase() : "ROLE_" + rol.toUpperCase());
        rolSocioService.asignarRol(requestDTO);
    }

    private void actualizarRolSiCorresponde(Socio socioExistente, String nuevoRol) {
        if (nuevoRol == null || nuevoRol.isBlank()) {
            return;
        }
        String rolNormalizado = nuevoRol.toUpperCase().startsWith("ROLE_") ? nuevoRol.toUpperCase() : "ROLE_" + nuevoRol.toUpperCase();
        String rolActual = obtenerRolActivo(socioExistente);

        if (rolActual != null && !rolActual.equalsIgnoreCase(rolNormalizado)) {
            RolSocioRequestDTO quitar = new RolSocioRequestDTO();
            quitar.setCorreoSocio(socioExistente.getCorreo());
            quitar.setNombreRol(rolActual);
            rolSocioService.quitarRol(quitar);
        }
        if (!rolNormalizado.equalsIgnoreCase(rolActual)) {
            RolSocioRequestDTO asignar = new RolSocioRequestDTO();
            asignar.setCorreoSocio(socioExistente.getCorreo());
            asignar.setNombreRol(rolNormalizado);
            rolSocioService.asignarRol(asignar);
        }
    }

    private String obtenerRolActivo(Socio socio) {
        if (socio.getRolSocios() == null) {
            return null;
        }
        return socio.getRolSocios().stream()
                .filter(rolSocio -> {
                    String estado = rolSocio.getEstado();
                    return "ACTIVO".equalsIgnoreCase(estado);
                })
                .map(rolSocio -> rolSocio.getRol().getDescripcion())
                .findFirst()
                .orElse(null);
    }

}
