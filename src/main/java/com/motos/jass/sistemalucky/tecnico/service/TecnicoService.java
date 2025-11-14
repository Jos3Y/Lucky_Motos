package com.motos.jass.sistemalucky.tecnico.service;

import com.motos.jass.sistemalucky.tecnico.dto.DisponibilidadRequestDTO;
import com.motos.jass.sistemalucky.tecnico.dto.TecnicoResponseDTO;

import java.util.List;

public interface TecnicoService {
    List<TecnicoResponseDTO> obtenerTodosLosTecnicos();
    TecnicoResponseDTO obtenerTecnicoPorId(Long id);
    TecnicoResponseDTO obtenerTecnicoPorSocioId(Long socioId);
    List<TecnicoResponseDTO> obtenerTecnicosDisponibles();
    void actualizarDisponibilidad(Long tecnicoId, DisponibilidadRequestDTO request);
}

