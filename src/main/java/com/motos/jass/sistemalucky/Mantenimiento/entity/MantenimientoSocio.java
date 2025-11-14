package com.motos.jass.sistemalucky.Mantenimiento.entity;

import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mantenimiento_socio")
public class MantenimientoSocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento_socio")
    private long id;

    @ManyToOne
    @JoinColumn(name = "mantenimiento_id", nullable = false)
    private Mantenimiento mantenimiento;

    @ManyToOne
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio socio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "rol_en_mantenimiento")
    private String rolEnMantenimiento;

}
