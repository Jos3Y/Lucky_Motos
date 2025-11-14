package com.motos.jass.sistemalucky.socio.service;

import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
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
public  class SocioServiceImpl extends BaseServiceImpl<Socio, Long> implements SocioService {

    private final PasswordEncoder passwordEncoder;
    private final SocioMapper socioMapper;
    private final RolSocioService rolSocioService;
    private final SocioRepository socioRepository;
    private final RolRepository rolRepository;
    private final RolSocioRepository rolSocioRepository;


    public SocioServiceImpl(SocioRepository repository, PasswordEncoder passwordEncoder, SocioMapper socioMapper, RolSocioService rolSocioService, SocioRepository socioRepository, RolRepository rolRepository, RolSocioRepository rolSocioRepository) {

        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.socioMapper = socioMapper;

        this.rolSocioService = rolSocioService;
        this.socioRepository = socioRepository;
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
        return socioMapper.toResponseDTO(savedSocio);
    }

    // lista solo la información del DTO
    public List<SocioResponseDTO> listarSociosDTO() {
        return socioRepository.findAll().stream()
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
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public SocioResponseDTO actualizarSocio(Long id, SocioRequestDTO request) {
        System.out.println("Entrando a actualizar socio ID: " + id);

        // 1 Buscar el socio existente
        Socio socioExistente = socioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + id));

        // 2 Actualizar manualmente los campos editables
        socioExistente.setNombre(request.getNombre());
        socioExistente.setApellidos(request.getApellidos());
        socioExistente.setDni(request.getDni());
        socioExistente.setCorreo(request.getCorreo());
        socioExistente.setTelefono(request.getTelefono());
        socioExistente.setGenero(request.getGenero());
        socioExistente.setFechaNacimiento(request.getFechaNacimiento());
        socioExistente.setEstadoCivil(request.getEstadoCivil());
        socioExistente.setContrasena(request.getContrasena());

        // 3 Guardar los cambios
        socioRepository.save(socioExistente);

        // 4 Retornar en formato DTO
        return socioMapper.toResponseDTO(socioExistente);
    }



}






