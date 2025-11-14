package com.motos.jass.sistemalucky.socio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name="rol_socio")
public class RolSocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rolSocio")

    private Long id;

    private String descripcion;

    private String estado;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="updated_At")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "socio_id")
    private Socio socio;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();  // Solo la primera vez
        updatedAt = LocalDateTime.now();  // También la primera vez
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();  // Cada vez que se actualiza
    }


}
