package com.motos.jass.sistemalucky.repuesto.repository;

import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepuestoRepository extends JpaRepository<Repuesto, Long> {
    List<Repuesto> findByMarca(String marca);
    List<Repuesto> findByModeloCompatible(String modeloCompatible);
    List<Repuesto> findByEstado(Repuesto.EstadoRepuesto estado);
    
    @Query("SELECT r FROM Repuesto r WHERE r.marca = :marca AND r.modeloCompatible = :modelo")
    List<Repuesto> findByMarcaAndModelo(@Param("marca") String marca, @Param("modelo") String modelo);

    List<Repuesto> findByNombreContainingIgnoreCase(String nombre);
    
    @Query("SELECT r FROM Repuesto r WHERE r.stock < 5")
    List<Repuesto> findBajoStock();
}

