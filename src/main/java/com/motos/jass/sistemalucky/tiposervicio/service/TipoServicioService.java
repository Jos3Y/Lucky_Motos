package com.motos.jass.sistemalucky.tiposervicio.service;

import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioRequestDTO;
import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioResponseDTO;

import java.util.List;

public interface TipoServicioService {
    List<TipoServicioResponseDTO> listarTodos();
    TipoServicioResponseDTO obtenerPorId(Long id);
    TipoServicioResponseDTO crear(TipoServicioRequestDTO request);
    TipoServicioResponseDTO actualizar(Long id, TipoServicioRequestDTO request);
    void eliminar(Long id);
}

