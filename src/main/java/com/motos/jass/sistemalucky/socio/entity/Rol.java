package com.motos.jass.sistemalucky.socio.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // opcional si quieres patrón builder
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")

    private long id;
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolSocio> RolSocios;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
