package com.motos.jass.sistemalucky.cita.entity;

import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cita_repuesto")
public class CitaRepuesto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita_repuesto")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "repuesto_id", nullable = false)
    private Repuesto repuesto;
    
    @Column(nullable = false)
    private Integer cantidad = 1;
    
    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precioUnitario;
    
    @PrePersist
    public void prePersist() {
        if (precioUnitario == null && repuesto != null) {
            precioUnitario = repuesto.getPrecio();
        }
    }
}

