package com.motos.jass.sistemalucky.vivienda.entity;

import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "socio_vivienda")
public class SocioVivienda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio_vivienda")
    private long idSocioVivienda;

    @ManyToOne
    @JoinColumn(name = "socio_id")
    private Socio socio;
    @ManyToOne

    @JoinColumn(name = "vivienda_id")
    private Vivienda vivienda;

    @Column(name = "rol_en_vivienda")
    private String rolEnVivienda;

    private String estado;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;


    private String observaciones;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDate updatedAt;

}
