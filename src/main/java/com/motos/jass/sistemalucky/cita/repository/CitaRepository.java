package com.motos.jass.sistemalucky.cita.repository;

import com.motos.jass.sistemalucky.cita.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    Optional<Cita> findByCodigoCita(String codigoCita);
    List<Cita> findByClienteId(Long clienteId);
    
    @Query("SELECT c FROM Cita c WHERE c.tecnico.id = :tecnicoId")
    List<Cita> findByTecnicoId(@Param("tecnicoId") Long tecnicoId);
    
    List<Cita> findByEstado(Cita.EstadoCita estado);
    List<Cita> findByFechaCita(LocalDate fecha);
    
    @Query("SELECT c FROM Cita c WHERE c.fechaCita = :fecha AND c.horaCita = :hora AND c.tecnico.id = :tecnicoId")
    List<Cita> findByFechaHoraAndTecnico(@Param("fecha") LocalDate fecha, @Param("hora") java.time.LocalTime hora, @Param("tecnicoId") Long tecnicoId);
    
    @Query("SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :fechaInicio AND :fechaFin")
    List<Cita> findByRangoFechas(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT c.tecnico.id, c.tecnico.socio.nombre, c.tecnico.socio.apellidos, COUNT(c) " +
            "FROM Cita c WHERE c.tecnico IS NOT NULL AND c.fechaCita BETWEEN :inicio AND :fin " +
            "GROUP BY c.tecnico.id, c.tecnico.socio.nombre, c.tecnico.socio.apellidos " +
            "ORDER BY COUNT(c) DESC")
    List<Object[]> contarCitasPorTecnico(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT c.fechaCita, COUNT(c) FROM Cita c WHERE c.fechaCita BETWEEN :inicio AND :fin " +
            "GROUP BY c.fechaCita ORDER BY COUNT(c) DESC")
    List<Object[]> contarCitasPorDia(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);
}

