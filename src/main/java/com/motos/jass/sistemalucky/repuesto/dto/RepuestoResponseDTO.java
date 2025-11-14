package com.motos.jass.sistemalucky.repuesto.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepuestoResponseDTO {
    private Long id;
    private String nombre;
    private String marca;
    private String modeloCompatible;
    private Integer stock;
    private Double precio;
    private String estado;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}

