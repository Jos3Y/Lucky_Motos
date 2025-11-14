package com.motos.jass.sistemalucky.instalacion.entity;

import com.motos.jass.sistemalucky.Arranque.entity.Arranque;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "instalacion")
public class Instalacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private long id;

    @OneToOne
    @JoinColumn(name = "arranque_id", nullable = false)
    private Arranque arranque;


    @ManyToOne
    @JoinColumn(name = "id_socio_registro", nullable = false)
    private Socio socioRegistroInstalacion;

    @OneToMany(mappedBy = "instalacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstalacionSocio> instalacionSocios = new ArrayList<>();

    @Column(name = "fecha_instlacion")
    private LocalDate fechaInstalacion;
    @Column(name = "costo_estimado")
    private double costoEstimado;


}
