package com.motos.jass.sistemalucky.moto.entity;


import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "moto")
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moto")
    private long id;

    private String placa;

    private String modelo;

    private String marca;

    private String tipoCombustible;

    private String nroSerieMotor;

    private String numeroChasis;

    private String kilometrajeActual;

    @Column(name = "estado", nullable = false)
    private String estado = "ACTIVA"; // valor por defecto al crear



    // Enum interno o externo
    public enum EstadoReserva {
        en_revisión,
        en_reparación,
        lista,
        entregada
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name="socio_id")
    private Socio socio;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL)
    private List<Reserva> reserva = new ArrayList<>();


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();  // Solo la primera vez
        updatedAt = LocalDateTime.now();  // También la primera vez
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();}


}
