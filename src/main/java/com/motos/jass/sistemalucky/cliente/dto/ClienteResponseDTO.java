package com.motos.jass.sistemalucky.cliente.dto;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String correo;
    private String telefono;
    private String direccion;
    private Boolean estado;
}

