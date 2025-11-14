package com.motos.jass.sistemalucky.repuesto.entity;

import com.motos.jass.sistemalucky.cita.entity.CitaRepuesto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "repuesto")
public class Repuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto")
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String nombre;
    
    @Column(length = 100)
    private String marca;
    
    @Column(name = "modelo_compatible", length = 100)
    private String modeloCompatible;
    
    @Column(nullable = false)
    private Integer stock = 0;
    
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precio = 0.0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRepuesto estado = EstadoRepuesto.DISPONIBLE;
    
    public enum EstadoRepuesto {
        DISPONIBLE,
        AGOTADO,
        BAJO_STOCK
    }
    
    @OneToMany(mappedBy = "repuesto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaRepuesto> citaRepuestos = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        actualizarEstado();
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        actualizarEstado();
    }
    
    private void actualizarEstado() {
        if (stock == 0) {
            estado = EstadoRepuesto.AGOTADO;
        } else if (stock < 5) {
            estado = EstadoRepuesto.BAJO_STOCK;
        } else {
            estado = EstadoRepuesto.DISPONIBLE;
        }
    }
}

