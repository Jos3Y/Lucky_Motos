package com.motos.jass.sistemalucky.Mantenimiento.entity;

import com.motos.jass.sistemalucky.Arranque.entity.Arranque;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "mantenimientos")
public class Mantenimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "arranque_id", referencedColumnName = "id_arranque", nullable = false)
    private Arranque arranque;

    @ManyToOne
    @JoinColumn(name = "socio_registro_id", nullable = false)
    private Socio socioRegistroMantenimiento;

    @OneToMany(mappedBy = "mantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MantenimientoSocio> MantenimientoSocios;

    @ManyToOne
    @JoinColumn(name ="categoria_mantenimiento_id", nullable = false)
    private CategoriaMantenimiento categoriaMantenimiento;

    @Column(name ="fecha_mantenimiento")
    private LocalDateTime fechaMantenimiento;
    @Column(name = "tipo_mantenimiento")
    private String tipoMantenimiento;
    @Column(name = "descripcion_mantenimiento")
    private String descripcionMantenimiento;









}
