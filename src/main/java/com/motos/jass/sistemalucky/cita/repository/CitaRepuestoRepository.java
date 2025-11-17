package com.motos.jass.sistemalucky.cita.repository;

import com.motos.jass.sistemalucky.cita.entity.CitaRepuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepuestoRepository extends JpaRepository<CitaRepuesto, Long> {

    @Query("SELECT cr.repuesto.nombre, SUM(cr.cantidad) FROM CitaRepuesto cr " +
            "WHERE cr.cita.fechaCita BETWEEN :inicio AND :fin " +
            "GROUP BY cr.repuesto.nombre ORDER BY SUM(cr.cantidad) DESC")
    List<Object[]> contarRepuestosUtilizados(@Param("inicio") LocalDate inicio,
                                             @Param("fin") LocalDate fin);
}

