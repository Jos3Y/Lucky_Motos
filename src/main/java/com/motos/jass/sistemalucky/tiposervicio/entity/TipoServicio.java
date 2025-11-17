package com.motos.jass.sistemalucky.tiposervicio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.motos.jass.sistemalucky.cita.entity.Cita;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_servicio")
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "precio_base", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double precioBase = 0.0;
    
    @Column(name = "duracion_estimada_minutos")
    private Integer duracionEstimadaMinutos;
    
    @OneToMany(mappedBy = "tipoServicio", cascade = CascadeType.ALL)
    @JsonIgnore // Evitar loop infinito en serialización JSON
    private List<Cita> citas = new ArrayList<>();
}

