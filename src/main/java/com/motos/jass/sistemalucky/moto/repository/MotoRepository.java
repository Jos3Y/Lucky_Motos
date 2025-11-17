package com.motos.jass.sistemalucky.moto.repository;

import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MotoRepository extends BaseRepository <Moto, Long>{


    Optional<Moto> findByplaca(String placa);

    @Query("SELECT m FROM Moto m WHERE m.estado = 'ACTIVO'")
    List<Moto> findAllActivas();

    List<Moto> findByClienteId(Long clienteId);

    @Query("SELECT DISTINCT m.modelo FROM Moto m WHERE m.estado = 'ACTIVO' ORDER BY m.modelo")
    List<String> findDistinctModelos();

    Optional<Moto> findFirstByModeloAndEstado(String modelo, Moto.EstadoMoto estado);

}
