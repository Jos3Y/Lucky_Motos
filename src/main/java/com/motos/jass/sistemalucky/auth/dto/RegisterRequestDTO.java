package com.motos.jass.sistemalucky.auth.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDTO {

    private String nombre;
    private String apellidos;
    private String dni;
    private String correo;
    private String telefono;
    private String genero;
    private LocalDate fechaNacimiento;
    private String estadoCivil;
    private String contrasena;
}
