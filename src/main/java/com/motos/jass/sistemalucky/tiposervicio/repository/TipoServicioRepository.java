package com.motos.jass.sistemalucky.tiposervicio.repository;

import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicioRepository extends JpaRepository<TipoServicio, Long> {
}

