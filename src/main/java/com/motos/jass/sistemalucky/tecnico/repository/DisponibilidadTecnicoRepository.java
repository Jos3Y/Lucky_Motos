package com.motos.jass.sistemalucky.tecnico.repository;

import com.motos.jass.sistemalucky.tecnico.entity.DisponibilidadTecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadTecnicoRepository extends JpaRepository<DisponibilidadTecnico, Long> {
    List<DisponibilidadTecnico> findByTecnicoId(Long tecnicoId);
    List<DisponibilidadTecnico> findByTecnicoIdAndDisponibleTrue(Long tecnicoId);
}

