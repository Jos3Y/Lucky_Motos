package com.motos.jass.sistemalucky.cliente.mapper;

import com.motos.jass.sistemalucky.cliente.dto.ClienteRequestDTO;
import com.motos.jass.sistemalucky.cliente.dto.ClienteResponseDTO;
import com.motos.jass.sistemalucky.cliente.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "motos", ignore = true)
    @Mapping(target = "citas", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estado", constant = "true")
    Cliente toEntity(ClienteRequestDTO dto);
    
    ClienteResponseDTO toResponseDTO(Cliente cliente);
}

