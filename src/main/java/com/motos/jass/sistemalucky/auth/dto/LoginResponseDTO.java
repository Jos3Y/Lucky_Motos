package com.motos.jass.sistemalucky.auth.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class LoginResponseDTO {
    private Long idSocio;
    private String nombre;
    private String apellidos;
    private String dni;
    private String correo;
    private String telefono;
    private String genero;
    private LocalDate fechaNacimiento;
    private String estadoCivil;
    private List<String> roles;
    private String token;
}
