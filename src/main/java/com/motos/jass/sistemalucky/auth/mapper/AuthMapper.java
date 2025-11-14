package com.motos.jass.sistemalucky.auth.mapper;


import com.motos.jass.sistemalucky.auth.dto.LoginResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.Socio;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "idSocio", source = "id")
    @Mapping(target = "roles", expression = "java( socio.getRolSocios().stream().map(r -> r.getRol().getDescripcion()).toList())")
    LoginResponseDTO socioToLoginResponseDTO(Socio socio);

}
