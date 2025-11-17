package com.motos.jass.sistemalucky.moto.dto;


import lombok.Data;


@Data
public class MotoRequestDTO {

    private String placa;
    private String modelo;
    private String marca;
    private Integer anio;
    private String tipoCombustible;
    private String nroSerieMotor;
    private String numeroChasis;
    private Integer kilometrajeActual;

    // Se asocia por el id del cliente existente
    private Long clienteId;
}
