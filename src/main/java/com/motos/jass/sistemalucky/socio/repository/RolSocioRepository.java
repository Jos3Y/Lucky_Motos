package com.motos.jass.sistemalucky.socio.repository;

import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import com.motos.jass.sistemalucky.socio.entity.Socio;

import java.util.Optional;

public interface RolSocioRepository extends BaseRepository<RolSocio, Long> {

    Optional<RolSocio> findBySocioAndRol(Socio socio, Rol rol);





}
