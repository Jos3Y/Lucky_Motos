package com.motos.jass.sistemalucky.socio.service;

import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
import com.motos.jass.sistemalucky.socio.dto.RolSocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolSocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.mapper.RolSocioMapper;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import com.motos.jass.sistemalucky.socio.repository.RolSocioRepository;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RolSocioServiceImpl extends BaseServiceImpl<RolSocio, Long> implements RolSocioService{

    private final SocioRepository socioRepository;
    private final RolRepository rolRepository;
    private final RolSocioRepository rolSocioRepository;
    private final RolSocioMapper rolSocioMapper;
    private  RolSocioRepository repository;
    private final RolSocioMapper mapper;

    public RolSocioServiceImpl(RolSocioRepository repository, RolSocioMapper mapper, SocioRepository socioRepository, RolRepository rolRepository, RolSocioRepository rolSocioRepository, RolSocioMapper rolSocioMapper) {
        super(repository);
        this.mapper = mapper;
        this.socioRepository = socioRepository;
        this.rolRepository = rolRepository;
        this.rolSocioRepository = rolSocioRepository;
        this.rolSocioMapper = rolSocioMapper;
    }
    @Transactional
    public RolSocioResponseDTO asignarRol(RolSocioRequestDTO request){

        Socio socio = socioRepository.findByCorreo(request.getCorreoSocio())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado por correo"));

        // Buscar rol por descripción
        Rol rol = rolRepository.findByDescripcion(request.getNombreRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado por nombre"));

        Optional<RolSocio> existente = rolSocioRepository.findBySocioAndRol(socio, rol);
        RolSocio rolSocio;

        if (existente.isPresent()){
            // Ya existe la relación → reactivar
            rolSocio = existente.get();
            rolSocio.setEstado("Activo");
            rolSocio.setDescripcion("Registro Encontrado - Estado Modificado");
        }else{
            //Relacion creada
            rolSocio = rolSocioMapper.toEntity(request);
            rolSocio.setSocio(socio);
            rolSocio.setRol(rol);
            rolSocio.setDescripcion("Registro Nuevo - Creado");
            rolSocio.setEstado("Activo");

        }
        RolSocio guardado = rolSocioRepository.save(rolSocio);
        return rolSocioMapper.toResponseDTO(guardado);
    }

    public RolSocioResponseDTO quitarRol(RolSocioRequestDTO request){

        Socio socio = socioRepository.findByCorreo(request.getCorreoSocio())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado por correo"));

        Rol rol = rolRepository.findByDescripcion(request.getNombreRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado por nombre"));

        RolSocio rolSocio = rolSocioRepository.findBySocioAndRol(socio, rol)
                .orElseThrow(() -> new RuntimeException("Relación socio-rol no encontrada"));

        rolSocio.setEstado("Inactivo");
        rolSocio.setDescripcion("Registro Borrado - Estado Eliminado");

        RolSocio guardado = rolSocioRepository.save(rolSocio);
        return rolSocioMapper.toResponseDTO(guardado);

    }

}
