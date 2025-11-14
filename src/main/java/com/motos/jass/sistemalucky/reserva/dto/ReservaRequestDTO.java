package com.motos.jass.sistemalucky.reserva.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservaRequestDTO {

    private String codigoReserva;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;
    @JsonFormat(pattern = "HH:mm") // si tu JSON envía solo horas y minutos, ej: "11:48"
    private LocalTime horaReserva;
    private String comentario;

    // Asociaciones (solo IDs)
    private Long socioId;
    private Long motoId;
    private Long socioRegistroResId; // el socio que registra la reserva

    // Estado funcional de la reserva (por ejemplo: PENDIENTE, CONFIRMADA, CANCELADA)
    private String estado;
}
