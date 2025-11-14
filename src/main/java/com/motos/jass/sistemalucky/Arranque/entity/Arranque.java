package com.motos.jass.sistemalucky.Arranque.entity;

import com.motos.jass.sistemalucky.Mantenimiento.entity.Mantenimiento;
import com.motos.jass.sistemalucky.instalacion.entity.Instalacion;
import com.motos.jass.sistemalucky.vivienda.entity.Vivienda;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "arranque")
public class Arranque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_arranque")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vivienda_id")  // Clave foránea hacia Vivienda
    private Vivienda vivienda;

    @OneToOne(mappedBy = "arranque", cascade = CascadeType.ALL)
    private Instalacion instalacion;

    @OneToMany(mappedBy = "arranque", cascade = CascadeType.ALL)
    private List<Mantenimiento> mantenimientos = new ArrayList<>();

    @Column(name = "estado_arranque")
    private String estadoArranque;

    @Column(name = "nro_arranque")
    private String nroArranque;

    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    @Column(name = "estado_instalacion")

    private LocalDate fechaInstalacion;
    @Column(name = "tipo_arranque")
    private String tipoArranque;


}
