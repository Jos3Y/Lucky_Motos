package com.motos.jass.sistemalucky.socio.repository;

import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import com.motos.jass.sistemalucky.socio.entity.Socio;

import java.util.Optional;

public  interface SocioRepository extends BaseRepository<Socio, Long> {


        Optional<Socio> findByCorreo(String correo);

}
