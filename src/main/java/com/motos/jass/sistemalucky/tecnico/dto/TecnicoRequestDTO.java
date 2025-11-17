package com.motos.jass.sistemalucky.tecnico.dto;

import lombok.Data;

@Data
public class TecnicoRequestDTO {
    private Long socioId;
    private String especialidad;
    private String estado;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
}