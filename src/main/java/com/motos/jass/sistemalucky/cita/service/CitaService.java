package com.motos.jass.sistemalucky.cita.service;

import com.motos.jass.sistemalucky.cita.dto.CitaRequestDTO;
import com.motos.jass.sistemalucky.cita.dto.CitaResponseDTO;
import com.motos.jass.sistemalucky.cita.dto.CitaUpdateEstadoDTO;

import java.util.List;

public interface CitaService {
    CitaResponseDTO crearCita(CitaRequestDTO request);
    CitaResponseDTO obtenerCitaPorId(Long id);
    List<CitaResponseDTO> obtenerTodasLasCitas();
    List<CitaResponseDTO> obtenerCitasPorCliente(Long clienteId);
    List<CitaResponseDTO> obtenerCitasPorTecnico(Long tecnicoId);
    List<CitaResponseDTO> obtenerCitasPorEstado(String estado);
    List<CitaResponseDTO> obtenerCitasDeHoy();
    List<CitaResponseDTO> obtenerCitasDeHoyPorTecnico(Long tecnicoId);
    CitaResponseDTO actualizarEstado(Long id, CitaUpdateEstadoDTO request);
    CitaResponseDTO actualizarCita(Long id, CitaRequestDTO request);
    void eliminarCita(Long id);
}

