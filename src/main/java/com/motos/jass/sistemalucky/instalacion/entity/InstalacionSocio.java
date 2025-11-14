package com.motos.jass.sistemalucky.instalacion.entity;

import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "instalacion_socio")
public class InstalacionSocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instalacion_socio")
    private long id;

    @ManyToOne
    @JoinColumn(name = "instalacion_id", nullable = false)
    private Instalacion instalacion;

    @ManyToOne
    @JoinColumn(name = "socios_id", nullable = false)
    private Socio socio;
    @Column(name = "rol_en_instalacion")
    private String rolEnInstalacion;
    @Column(name = "created_At")
    private LocalDate createdAt;


}
