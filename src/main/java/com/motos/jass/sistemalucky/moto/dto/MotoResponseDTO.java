package com.motos.jass.sistemalucky.moto.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MotoResponseDTO
{

    private Long id;
    private String placa;
    private String modelo;
    private String marca;
    private String tipoCombustible;
    private String nroSerieMotor;
    private String numeroChasis;
    private String kilometrajeActual;

    private String estado;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Datos del socio (solo lo esencial)
    private Long socioId;
    private String nombreSocio;
}
