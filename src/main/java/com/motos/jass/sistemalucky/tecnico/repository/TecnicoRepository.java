package com.motos.jass.sistemalucky.tecnico.repository;

import com.motos.jass.sistemalucky.tecnico.entity.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, Long> {
    Optional<Tecnico> findBySocioId(Long socioId);
    List<Tecnico> findByEstado(Tecnico.EstadoTecnico estado);
    boolean existsBySocioId(Long socioId);
}

