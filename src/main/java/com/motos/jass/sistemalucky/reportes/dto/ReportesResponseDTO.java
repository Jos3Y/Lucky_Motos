package com.motos.jass.sistemalucky.reportes.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReportesResponseDTO {
    private List<ReporteTecnicoDTO> mejoresTecnicos;
    private List<ReporteDiaDTO> diasConMasCitas;
    private List<ReporteRepuestoDTO> repuestosMasUsados;

    @Data
    public static class ReporteTecnicoDTO {
        private Long tecnicoId;
        private String nombreCompleto;
        private Long totalCitas;
    }

    @Data
    public static class ReporteDiaDTO {
        private String fecha;
        private Long totalCitas;
    }

    @Data
    public static class ReporteRepuestoDTO {
        private String nombreRepuesto;
        private Long cantidad;
    }
}

