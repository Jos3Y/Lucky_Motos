package com.motos.jass.sistemalucky.moto.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motos.jass.sistemalucky.cliente.entity.Cliente;
import com.motos.jass.sistemalucky.reserva.entity.Reserva;
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
    private Long id;

    @Column(nullable = false, length = 20)
    private String placa;

    @Column(nullable = false, length = 100)
    private String modelo;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "tipo_combustible", length = 50)
    private String tipoCombustible;

    @Column(name = "nro_serie_motor", length = 100)
    private String nroSerieMotor;

    @Column(name = "numero_chasis", length = 100)
    private String numeroChasis;

    @Column(name = "kilometraje_actual")
    private Integer kilometrajeActual;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoMoto estado = EstadoMoto.ACTIVO;

    public enum EstadoMoto {
        ACTIVO,
        EN_REPARACION,
        VENDIDA,
        INACTIVO
    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL)
    @JsonIgnore
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
