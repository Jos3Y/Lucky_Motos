package com.motos.jass.sistemalucky.socio.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RolSocioResponseDTO {

    private Long id;
    private String socioNombre;
    private String socioApellido;
    private String rolNombre;
    private String correoSocio; // ✅ agregado

    private String estado;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
