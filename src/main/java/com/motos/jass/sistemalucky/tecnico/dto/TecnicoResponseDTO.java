package com.motos.jass.sistemalucky.tecnico.dto;

import lombok.Data;
import java.util.List;

@Data
public class TecnicoResponseDTO {
    private Long id;
    private Long socioId;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String especialidad;
    private String estado;
    private List<DisponibilidadDTO> disponibilidades;
    
    @Data
    public static class DisponibilidadDTO {
        private Long id;
        private String diaSemana;
        private String horaInicio;
        private String horaFin;
        private Boolean disponible;
    }
}

