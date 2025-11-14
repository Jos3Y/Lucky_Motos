package com.motos.jass.sistemalucky.reserva.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id_reserva")
    private long id;

    private String codigoReserva;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "hora_reserva")
    private LocalTime horaReserva;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_reserva")
    private LocalDate fechaReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reserva", nullable = false)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    // Enum interno o externo
    public enum EstadoReserva {
        PENDIENTE,
        FINALIZADA,
        CANCELADA
    }
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_registro", nullable = false)
    private EstadoRegistro estadoRegistro = EstadoRegistro.ACTIVO;

    public enum EstadoRegistro {
        ACTIVO,
        ELIMINADO
    }


    @Column(columnDefinition = "TEXT")
    private String comentario;

    @ManyToOne
    @JoinColumn(name="socio_id")
    private Socio socio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="moto_id")
    private Moto moto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_por", referencedColumnName = "id_socio")
    private Socio socioRegistroRes;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();  // Solo la primera vez
        updatedAt = LocalDateTime.now();  // También la primera vez
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();  // Cada vez que se actualiza
    }
}
