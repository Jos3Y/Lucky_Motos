package com.motos.jass.sistemalucky.socio.service;

import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
import com.motos.jass.sistemalucky.socio.dto.RolRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.mapper.RolMapper;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServiceImpl extends BaseServiceImpl<Rol, Long>
        implements RolService {

    private final RolMapper rolMapper;
    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository repository, RolMapper rolMapper, RolRepository rolRepository) {

        super(repository);
        this.rolMapper = rolMapper;
        this.rolRepository = rolRepository;
    }

    @Transactional
    public RolResponseDTO crearRol(RolRequestDTO dto) {
        System.out.println("Entrando a la peticion RolRequestDTO"+dto.toString());
        Rol rol = rolMapper.toEntity(dto);
        Rol rolGuardado = rolRepository.save(rol);
        return rolMapper.toResponseDTO(rolGuardado);
    }


}
