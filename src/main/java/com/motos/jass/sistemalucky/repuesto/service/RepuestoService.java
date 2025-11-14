package com.motos.jass.sistemalucky.repuesto.service;

import com.motos.jass.sistemalucky.repuesto.dto.RepuestoRequestDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoResponseDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoFilterDTO;

import java.util.List;

public interface RepuestoService {
    RepuestoResponseDTO crearRepuesto(RepuestoRequestDTO request);
    RepuestoResponseDTO obtenerRepuestoPorId(Long id);
    List<RepuestoResponseDTO> obtenerTodosLosRepuestos();
    List<RepuestoResponseDTO> filtrarRepuestos(RepuestoFilterDTO filter);
    RepuestoResponseDTO actualizarRepuesto(Long id, RepuestoRequestDTO request);
    void eliminarRepuesto(Long id);
    List<RepuestoResponseDTO> obtenerRepuestosBajoStock();
}

