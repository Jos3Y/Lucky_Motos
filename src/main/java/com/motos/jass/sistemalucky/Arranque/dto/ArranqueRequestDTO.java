package com.motos.jass.sistemalucky.Arranque.dto;

import lombok.Data;

@Data
public class ArranqueRequestDTO {
    private Long viviendaId;          // Solo enviamos el ID de la vivienda
    private String tipoArranque;
}
