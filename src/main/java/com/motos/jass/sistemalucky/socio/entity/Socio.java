package com.motos.jass.sistemalucky.socio.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.motos.jass.sistemalucky.Mantenimiento.entity.Mantenimiento;
import com.motos.jass.sistemalucky.Mantenimiento.entity.MantenimientoSocio;
import com.motos.jass.sistemalucky.instalacion.entity.Instalacion;
import com.motos.jass.sistemalucky.instalacion.entity.InstalacionSocio;
import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import com.motos.jass.sistemalucky.vivienda.entity.SocioVivienda;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // opcional si quieres patrón builder
@Entity
@Table(name = "socio")
public class Socio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")

    private long id;

    private String nombre;

    private String apellidos;
    private String telefono;
    private String dni;
    private String correo;
    @Column(name = "fecha_registro")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro;
    private Boolean estado = true;
    private String genero;
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    @Column(name = "estado_civil")
    private String estadoCivil;
    private String contrasena;



    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL)
    private List<Reserva> reservaS = new ArrayList<>();

    // Relación con Moto eliminada - ahora Moto pertenece a Cliente, no a Socio

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RolSocio> RolSocios = new ArrayList<>();

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<SocioVivienda> socioViviendas = new ArrayList<>();

    @OneToMany(mappedBy = "socioRegistroRes", cascade = CascadeType.ALL)
    private List<Reserva> sociosRegistroRes = new ArrayList<>();


    @OneToMany(mappedBy = "socioRegistroInstalacion", cascade = CascadeType.ALL  )
    private List<Instalacion> socioregistrosInstalaciones = new ArrayList<>();

    @OneToMany(mappedBy = "socioRegistroMantenimiento", cascade = CascadeType.ALL )
    private List<Mantenimiento>  socioregistrosMantenimientos = new ArrayList<>();

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InstalacionSocio> instalacionesSocios = new ArrayList<>();

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MantenimientoSocio> mantenimientosSocios = new ArrayList<>();

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
