package com.motos.jass.sistemalucky.Mantenimiento.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "categoria_mantenimiento")
public class CategoriaMantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;

    @Column(name = "nombre_categoria")
    private String nombreCategoria;

    @Column(name = "descripcion_categoria")
    private String descripcionCategoria;

    @Column(name = "costo_base")

    private Double costoBase;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "categoriaMantenimiento")
    private List<Mantenimiento>Mantenimientos = new ArrayList<>();



}
