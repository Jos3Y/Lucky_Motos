package com.motos.jass.sistemalucky.moto.dto;


import lombok.Data;


@Data
public class MotoRequestDTO {

    private String placa;
    private String modelo;
    private String marca;
    private String tipoCombustible;
    private String nroSerieMotor;
    private String numeroChasis;
    private String kilometrajeActual;

    // Se asocia por el id del socio existente
    private Long socioId;
}
