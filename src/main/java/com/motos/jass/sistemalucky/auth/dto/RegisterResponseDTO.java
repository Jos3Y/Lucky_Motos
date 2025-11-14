package com.motos.jass.sistemalucky.auth.dto;

import lombok.Data;

import java.util.List;
@Data
public class RegisterResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private List<String> roles; // Ej: ["ADMIN", "SOCIO"]
}
