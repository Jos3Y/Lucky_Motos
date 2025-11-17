package com.motos.jass.sistemalucky.cita.service;

import com.motos.jass.sistemalucky.cita.dto.*;
import com.motos.jass.sistemalucky.cita.entity.Cita;
import com.motos.jass.sistemalucky.cita.entity.CitaRepuesto;
import com.motos.jass.sistemalucky.cita.mapper.CitaMapper;
import com.motos.jass.sistemalucky.cita.repository.CitaRepository;
import com.motos.jass.sistemalucky.cliente.entity.Cliente;
import com.motos.jass.sistemalucky.cliente.repository.ClienteRepository;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.moto.repository.MotoRepository;
import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import com.motos.jass.sistemalucky.repuesto.repository.RepuestoRepository;
import com.motos.jass.sistemalucky.tecnico.entity.Tecnico;
import com.motos.jass.sistemalucky.tecnico.repository.TecnicoRepository;
import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import com.motos.jass.sistemalucky.tiposervicio.repository.TipoServicioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {
    
    private final CitaRepository citaRepository;
    private final CitaMapper citaMapper;
    private final ClienteRepository clienteRepository;
    private final MotoRepository motoRepository;
    private final TecnicoRepository tecnicoRepository;
    private final TipoServicioRepository tipoServicioRepository;
    private final RepuestoRepository repuestoRepository;
    
    @Override
    @Transactional
    public CitaResponseDTO crearCita(CitaRequestDTO request) {
        // Validar y obtener entidades
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Moto moto = motoRepository.findById(request.getMotoId())
                .orElseThrow(() -> new RuntimeException("Moto no encontrada"));
        
        Tecnico tecnico = null;
        if (request.getTecnicoId() != null) {
            tecnico = tecnicoRepository.findById(request.getTecnicoId())
                    .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        }
        
        TipoServicio tipoServicio = null;
        if (request.getTipoServicioId() != null) {
            tipoServicio = tipoServicioRepository.findById(request.getTipoServicioId())
                    .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));
        }
        
        // Crear cita
        Cita cita = citaMapper.toEntity(request);
        cita.setCliente(cliente);
        cita.setMoto(moto);
        cita.setTecnico(tecnico);
        cita.setTipoServicio(tipoServicio);
        
        // Asegurar que el estado tenga un valor por defecto si es null
        if (cita.getEstado() == null) {
            cita.setEstado(Cita.EstadoCita.PENDIENTE);
        }
        
        // Asegurar que el comprobante se guarde si existe
        if (request.getComprobantePagoUrl() != null) {
            cita.setComprobantePagoUrl(request.getComprobantePagoUrl());
        }
        
        // Agregar repuestos si existen
        if (request.getRepuestos() != null && !request.getRepuestos().isEmpty()) {
            for (CitaRequestDTO.RepuestoCitaDTO repuestoDTO : request.getRepuestos()) {
                Repuesto repuesto = repuestoRepository.findById(repuestoDTO.getRepuestoId())
                        .orElseThrow(() -> new RuntimeException("Repuesto no encontrado: " + repuestoDTO.getRepuestoId()));
                
                CitaRepuesto citaRepuesto = CitaRepuesto.builder()
                        .cita(cita)
                        .repuesto(repuesto)
                        .cantidad(repuestoDTO.getCantidad())
                        .precioUnitario(repuesto.getPrecio())
                        .build();
                
                cita.getRepuestos().add(citaRepuesto);
            }
        }
        
        Cita citaGuardada = citaRepository.save(cita);
        return citaMapper.toResponseDTO(citaGuardada);
    }
    
    @Override
    public CitaResponseDTO obtenerCitaPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return citaMapper.toResponseDTO(cita);
    }
    
    @Override
    public List<CitaResponseDTO> obtenerTodasLasCitas() {
        return citaRepository.findAll().stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CitaResponseDTO> obtenerCitasPorCliente(Long clienteId) {
        return citaRepository.findByClienteId(clienteId).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CitaResponseDTO> obtenerCitasPorTecnico(Long tecnicoId) {
        return citaRepository.findByTecnicoId(tecnicoId).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CitaResponseDTO> obtenerCitasPorEstado(String estado) {
        Cita.EstadoCita estadoEnum = Cita.EstadoCita.valueOf(estado.toUpperCase());
        return citaRepository.findByEstado(estadoEnum).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CitaResponseDTO> obtenerCitasDeHoy() {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        return citaRepository.findByFechaCita(hoy).stream()
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CitaResponseDTO> obtenerCitasDeHoyPorTecnico(Long tecnicoId) {
        java.time.LocalDate hoy = java.time.LocalDate.now();
        return citaRepository.findByTecnicoId(tecnicoId).stream()
                .filter(cita -> cita.getFechaCita().equals(hoy))
                .map(citaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CitaResponseDTO actualizarEstado(Long id, CitaUpdateEstadoDTO request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        cita.setEstado(Cita.EstadoCita.valueOf(request.getEstado().toUpperCase()));
        cita.setMotivoEstado(request.getMotivoEstado());
        
        Cita citaActualizada = citaRepository.save(cita);
        return citaMapper.toResponseDTO(citaActualizada);
    }
    
    @Override
    @Transactional
    public CitaResponseDTO actualizarCita(Long id, CitaRequestDTO request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        
        // Actualizar campos básicos
        if (request.getFechaCita() != null) cita.setFechaCita(request.getFechaCita());
        if (request.getHoraCita() != null) cita.setHoraCita(request.getHoraCita());
        if (request.getPagoInicial() != null) cita.setPagoInicial(request.getPagoInicial());
        if (request.getMontoPagoInicial() != null) cita.setMontoPagoInicial(request.getMontoPagoInicial());
        if (request.getComprobantePagoUrl() != null) cita.setComprobantePagoUrl(request.getComprobantePagoUrl());
        if (request.getObservaciones() != null) cita.setObservaciones(request.getObservaciones());
        
        // Actualizar relaciones
        if (request.getTecnicoId() != null) {
            Tecnico tecnico = tecnicoRepository.findById(request.getTecnicoId())
                    .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
            cita.setTecnico(tecnico);
        }
        
        if (request.getTipoServicioId() != null) {
            TipoServicio tipoServicio = tipoServicioRepository.findById(request.getTipoServicioId())
                    .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));
            cita.setTipoServicio(tipoServicio);
        }
        
        Cita citaActualizada = citaRepository.save(cita);
        return citaMapper.toResponseDTO(citaActualizada);
    }
    
    @Override
    @Transactional
    public void eliminarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        citaRepository.delete(cita);
    }
}

