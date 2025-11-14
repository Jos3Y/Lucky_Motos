package com.motos.jass.sistemalucky.socio.repository;

import com.motos.jass.sistemalucky.share.repository.BaseRepository;
import com.motos.jass.sistemalucky.socio.entity.Rol;

import java.util.Optional;

    public interface RolRepository extends BaseRepository<Rol, Long> {

    Optional<Rol> findByDescripcion(String descripcion);



}
