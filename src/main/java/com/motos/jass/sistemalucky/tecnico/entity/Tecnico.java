package com.motos.jass.sistemalucky.tecnico.entity;

import com.motos.jass.sistemalucky.cita.entity.Cita;
import com.motos.jass.sistemalucky.socio.entity.Socio;
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
@Table(name = "tecnico")
public class Tecnico {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "socio_id", nullable = false, unique = true)
    private Socio socio;
    
    @Column(length = 100)
    private String especialidad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoTecnico estado = EstadoTecnico.DISPONIBLE;
    
    public enum EstadoTecnico {
        DISPONIBLE,
        OCUPADO,
        INACTIVO
    }
    
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DisponibilidadTecnico> disponibilidades = new ArrayList<>();
    
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL)
    private List<Cita> citas = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

