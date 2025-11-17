package com.motos.jass.sistemalucky.tiposervicio.dto;

import lombok.Data;

@Data
public class TipoServicioResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precioBase;
    private Integer duracionEstimadaMinutos;
}

