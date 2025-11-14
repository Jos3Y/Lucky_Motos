package com.motos.jass.sistemalucky.socio.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class SocioResponseDTO {

    private Long idSocio;
    private String nombres;
    private String apellidos;
    private String dni;
    private String correo;
    private String telefono;
    private String genero;
    private LocalDate fechaNacimiento;
    private String estadoCivil;
}
