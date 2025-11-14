package com.motos.jass.sistemalucky.reserva.mapper;

import com.motos.jass.sistemalucky.reserva.dto.ReservaRequestDTO;
import com.motos.jass.sistemalucky.reserva.dto.ReservaResponseDTO;
import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring")
public interface ReservaMapper {


    // 🔹 De DTO → Entidad
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "socio.id", source = "socioId"),
            @Mapping(target = "moto.id", source = "motoId"),

            // ⚠️ Ignoramos el socioRegistroRes para asignarlo manualmente en el service
            @Mapping(target = "socioRegistroRes", ignore = true),

            @Mapping(target = "estado", expression = "java(mapEstado(request.getEstado()))"),
            @Mapping(target = "estadoRegistro", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),

            // Campos de fecha/hora
            @Mapping(target = "fechaReserva", source = "fechaReserva"),
            @Mapping(target = "horaReserva", source = "horaReserva")
    })
    Reserva toEntity(ReservaRequestDTO request);


    // 🔹 De Entidad → DTO
    @Mappings({
            @Mapping(target = "socioId", source = "socio.id"),
            @Mapping(target = "motoId", source = "moto.id"),
            @Mapping(target = "socioRegistroResId", source = "socioRegistroRes.id"),

            @Mapping(target = "nombreSocio", expression = "java(reserva.getSocio() != null ? reserva.getSocio().getNombre() + ' ' + reserva.getSocio().getApellidos() : null)"),
            @Mapping(target = "placaMoto", expression = "java(reserva.getMoto() != null ? reserva.getMoto().getPlaca() : null)"),
            @Mapping(target = "nombreSocioRegistro", expression = "java(reserva.getSocioRegistroRes() != null ? reserva.getSocioRegistroRes().getNombre() + ' ' + reserva.getSocioRegistroRes().getApellidos() : null)"),

            @Mapping(target = "fechaReserva", source = "fechaReserva"),
            @Mapping(target = "horaReserva", source = "horaReserva")
    })
    ReservaResponseDTO toResponseDTO(Reserva reserva);

    // 🔹 Método auxiliar para convertir String → Enum
    default Reserva.EstadoReserva mapEstado(String estado) {
        if (estado == null) {
            return Reserva.EstadoReserva.PENDIENTE; // valor por defecto
        }
        try {
            return Reserva.EstadoReserva.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Reserva.EstadoReserva.PENDIENTE; // fallback si no coincide
        }
    }
}
