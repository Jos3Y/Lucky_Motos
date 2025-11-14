package com.motos.jass.sistemalucky.cita.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class CitaResponseDTO {
    private Long id;
    private String codigoCita;
    private ClienteDTO cliente;
    private MotoDTO moto;
    private TecnicoDTO tecnico;
    private TipoServicioDTO tipoServicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaCita;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaCita;

    private String estado;
    private Boolean pagoInicial;
    private Double montoPagoInicial;
    private String comprobantePagoUrl;
    private String observaciones;
    private String motivoEstado;
    private List<RepuestoCitaDTO> repuestos;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @Data
    public static class ClienteDTO {
        private Long id;
        private String nombre;
        private String apellidos;
        private String dni;
        private String telefono;
        private String correo;
    }

    @Data
    public static class MotoDTO {
        private Long id;
        private String placa;
        private String marca;
        private String modelo;
    }

    @Data
    public static class TecnicoDTO {
        private Long id;
        private String nombre;
        private String apellidos;
        private String especialidad;
        private String estado;
    }

    @Data
    public static class TipoServicioDTO {
        private Long id;
        private String nombre;
        private String descripcion;
        private Double precioBase;
    }

    @Data
    public static class RepuestoCitaDTO {
        private Long id;
        private String nombre;
        private Integer cantidad;
        private Double precioUnitario;
    }
}
