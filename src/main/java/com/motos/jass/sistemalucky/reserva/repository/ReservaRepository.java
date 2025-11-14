package com.motos.jass.sistemalucky.reserva.repository;

import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import com.motos.jass.sistemalucky.socio.entity.Rol;

import java.util.Optional;

public interface ReservaRepository extends BaseRepository<Reserva, Long> {


    Optional<Reserva> findBycodigoReserva(String codigoReserva);

}
