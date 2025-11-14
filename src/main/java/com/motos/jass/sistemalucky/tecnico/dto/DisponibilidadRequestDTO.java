package com.motos.jass.sistemalucky.tecnico.dto;

import lombok.Data;
import java.util.List;

@Data
public class DisponibilidadRequestDTO {
    private Long tecnicoId;
    private List<DisponibilidadDTO> disponibilidades;
    
    @Data
    public static class DisponibilidadDTO {
        private String diaSemana;
        private String horaInicio;
        private String horaFin;
        private Boolean disponible;
    }
}

