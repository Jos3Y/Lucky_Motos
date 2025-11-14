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
            @Mapping(target = "socio.id", source = "socioId"),
            @Mapping(target = "reserva", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "estado", ignore = true) // 🔹 Lo controlamos en el servicio
    })
    Moto toEntity(MotoRequestDTO dto);

    // 🔹 Convierte de Entity a ResponseDTO
    @Mappings({
            @Mapping(target = "socioId", source = "socio.id"),
            @Mapping(
                    target = "nombreSocio",
                    expression = "java(moto.getSocio() != null ? moto.getSocio().getNombre() + ' ' + moto.getSocio().getApellidos() : null)"
            ),
            @Mapping(target = "estado", source = "estado") //  Lo mapeamos al DTO de salida

    })
    MotoResponseDTO toResponseDTO(Moto moto);
}
