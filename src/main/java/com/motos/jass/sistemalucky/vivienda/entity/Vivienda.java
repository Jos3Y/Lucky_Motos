package com.motos.jass.sistemalucky.vivienda.entity;

import com.motos.jass.sistemalucky.Arranque.entity.Arranque;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Table (name = "vivienda")
@Entity
public class Vivienda {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vivienda")
    private long id;

    private String direccion;
    @Column(name = "numero_lote")

    private String numeroLote;

    private String manzana;
    @Column(name = "nro_familias")
    private int nroFamilias;
    private boolean activo;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private Sector sector;

    @OneToMany(mappedBy = "vivienda", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<SocioVivienda> socioViviendas = new ArrayList<>();

    @OneToMany(mappedBy = "vivienda", cascade = CascadeType.ALL)
    private List<Arranque> arranques = new ArrayList<>();

}
