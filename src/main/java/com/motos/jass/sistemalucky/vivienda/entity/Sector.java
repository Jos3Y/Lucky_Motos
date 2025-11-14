package com.motos.jass.sistemalucky.vivienda.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class    Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private long sectorId;
    @Column(name = "nombre_sector") 
    private String nombreSector;
    private String descripcion;

}
