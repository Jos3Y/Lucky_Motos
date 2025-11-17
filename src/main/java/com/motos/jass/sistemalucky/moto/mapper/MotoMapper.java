package com.motos.jass.sistemalucky.moto.mapper;

import com.motos.jass.sistemalucky.moto.dto.MotoRequestDTO;
import com.motos.jass.sistemalucky.moto.dto.MotoResponseDTO;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MotoMapper {

    // 🔹 Convierte de RequestDTO a Entity
    @Mappings({
            @Mapping(target = "id", ignore = true), // Se ignora porque el RequestDTO no tiene ID
            @Mapping(target = "cliente.id", source = "clienteId"),
            @Mapping(target = "reserva", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "estado", ignore = true) // 🔹 Lo controlamos en el servicio
    })
    Moto toEntity(MotoRequestDTO dto);

    // 🔹 Convierte de Entity a ResponseDTO
    @Mappings({
            @Mapping(target = "clienteId", source = "cliente.id"),
            @Mapping(
                    target = "nombreCliente",
                    expression = "java(moto.getCliente() != null ? moto.getCliente().getNombre() + ' ' + moto.getCliente().getApellidos() : null)"
            ),
            @Mapping(target = "estado", source = "estado", defaultValue = "ACTIVO") //  Lo mapeamos al DTO de salida

    })
    MotoResponseDTO toResponseDTO(Moto moto);
}
