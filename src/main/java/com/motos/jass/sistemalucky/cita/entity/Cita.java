package com.motos.jass.sistemalucky.cita.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.tecnico.entity.Tecnico;
import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cita")
public class Cita {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;
    
    @Column(name = "codigo_cita", unique = true, length = 50)
    private String codigoCita;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "socio_id", nullable = false)
    private Socio cliente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "moto_id", nullable = false)
    private Moto moto;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_servicio_id")
    private TipoServicio tipoServicio;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;
    
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;
    
    public enum EstadoCita {
        PENDIENTE,
        CONFIRMADA,
        EN_PROCESO,
        COMPLETADA,
        CANCELADA
    }
    
    @Column(name = "pago_inicial", nullable = false)
    private Boolean pagoInicial = false;
    
    @Column(name = "monto_pago_inicial", columnDefinition = "DECIMAL(10,2)")
    private Double montoPagoInicial = 0.0;
    
    @Column(name = "comprobante_pago_url", length = 500)
    private String comprobantePagoUrl;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "motivo_estado", length = 500)
    private String motivoEstado;
    
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CitaRepuesto> repuestos = new ArrayList<>();
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (codigoCita == null || codigoCita.isEmpty()) {
            codigoCita = "CITA-" + System.currentTimeMillis();
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

