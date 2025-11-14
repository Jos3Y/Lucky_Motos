package com.motos.jass.sistemalucky.reserva.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class ReservaResponseDTO {

    private Long id;
    private String codigoReserva;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaReserva;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "hora_reserva")
    private LocalTime horaReserva;
    private String estado;
    private String comentario;
    private String estadoRegistro;


    // 🔹 Datos del socio principal
    private Long socioId;
    private String nombreSocio;

    // 🔹 Datos de la moto asociada
    private Long motoId;
    private String placaMoto;

    // 🔹 Datos del socio que registró la reserva
    private Long socioRegistroResId;
    private String nombreSocioRegistro;
}
