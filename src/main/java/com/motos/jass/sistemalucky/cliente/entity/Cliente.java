package com.motos.jass.sistemalucky.cliente.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motos.jass.sistemalucky.cita.entity.Cita;
import com.motos.jass.sistemalucky.moto.entity.Moto;
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
@Table(name = "cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String nombre;
    
    @Column(nullable = false, length = 255)
    private String apellidos;
    
    @Column(length = 20)
    private String dni;
    
    @Column(length = 255)
    private String correo;
    
    @Column(nullable = false, length = 20)
    private String telefono;
    
    @Column(length = 500)
    private String direccion;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
    private List<Moto> motos = new ArrayList<>();
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    @Builder.Default
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

