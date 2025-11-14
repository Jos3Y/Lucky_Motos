package com.motos.jass.sistemalucky.Arranque.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ArranqueResponseDTO {
    private Long id;
    private String estadoArranque;
    private String nroArranque;
    private LocalDate fechaSolicitud;
    private LocalDate fechaInstalacion;
    private String tipoArranque;

    // Datos básicos de la vivienda
    private Long viviendaId;
    private String direccionVivienda; // Para mostrar en la vista
}
