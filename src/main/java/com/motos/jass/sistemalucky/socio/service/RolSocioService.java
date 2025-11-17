package com.motos.jass.sistemalucky.socio.service;

import com.motos.jass.sistemalucky.share.service.BaseService;
import com.motos.jass.sistemalucky.socio.dto.RolSocioRequestDTO;
import com.motos.jass.sistemalucky.socio.dto.RolSocioResponseDTO;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;

public interface RolSocioService extends BaseService<RolSocio, Long> {

    RolSocioResponseDTO asignarRol(RolSocioRequestDTO request);

    RolSocioResponseDTO quitarRol(RolSocioRequestDTO request);
}
