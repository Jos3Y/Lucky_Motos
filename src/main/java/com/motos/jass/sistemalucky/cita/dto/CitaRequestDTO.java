package com.motos.jass.sistemalucky.cita.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CitaRequestDTO {
    private Long clienteId;
    private Long motoId;
    private Long tecnicoId;
    private Long tipoServicioId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCita;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaCita;

    private Boolean pagoInicial;
    private Double montoPagoInicial;
    private String comprobantePagoUrl;
    private String observaciones;
    private List<RepuestoCitaDTO> repuestos;

    @Data
    public static class RepuestoCitaDTO {
        private Long repuestoId;
        private Integer cantidad;
    }
}
