package com.motos.jass.sistemalucky.Arranque.mapper;
import com.motos.jass.sistemalucky.Arranque.dto.ArranqueRequestDTO;
import com.motos.jass.sistemalucky.Arranque.dto.ArranqueResponseDTO;
import com.motos.jass.sistemalucky.Arranque.entity.Arranque;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ArranqueMapper {

    // DTO → Entity
    @Mapping(target = "vivienda", ignore = true)
    Arranque toEntity(ArranqueRequestDTO dto);

    // Entity → DTO
    @Mapping(source = "vivienda.id", target = "viviendaId")
    @Mapping(source = "vivienda.direccion", target = "direccionVivienda")
    ArranqueResponseDTO toResponseDTO(Arranque entity);
}
