package com.motos.jass.sistemalucky.repuesto.dto;

import lombok.Data;

@Data
public class RepuestoRequestDTO {
    private String nombre;
    private String marca;
    private String modeloCompatible;
    private Integer stock;
    private Double precio;
}

