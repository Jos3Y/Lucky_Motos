package com.motos.jass.sistemalucky.tecnico.service;

import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import com.motos.jass.sistemalucky.tecnico.dto.DisponibilidadRequestDTO;
import com.motos.jass.sistemalucky.tecnico.dto.TecnicoResponseDTO;
import com.motos.jass.sistemalucky.tecnico.entity.DisponibilidadTecnico;
import com.motos.jass.sistemalucky.tecnico.entity.Tecnico;
import com.motos.jass.sistemalucky.tecnico.mapper.TecnicoMapper;
import com.motos.jass.sistemalucky.tecnico.repository.DisponibilidadTecnicoRepository;
import com.motos.jass.sistemalucky.tecnico.repository.TecnicoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TecnicoServiceImpl implements TecnicoService {
    
    private final TecnicoRepository tecnicoRepository;
    private final DisponibilidadTecnicoRepository disponibilidadRepository;
    private final TecnicoMapper tecnicoMapper;
    private final SocioRepository socioRepository;
    
    @Override
    public List<TecnicoResponseDTO> obtenerTodosLosTecnicos() {
        return tecnicoRepository.findAll().stream()
                .map(tecnicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public TecnicoResponseDTO obtenerTecnicoPorId(Long id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        return tecnicoMapper.toResponseDTO(tecnico);
    }
    
    @Override
    public TecnicoResponseDTO obtenerTecnicoPorSocioId(Long socioId) {
        Tecnico tecnico = tecnicoRepository.findBySocioId(socioId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        return tecnicoMapper.toResponseDTO(tecnico);
    }
    
    @Override
    public List<TecnicoResponseDTO> obtenerTecnicosDisponibles() {
        return tecnicoRepository.findByEstado(Tecnico.EstadoTecnico.DISPONIBLE).stream()
                .map(tecnicoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void actualizarDisponibilidad(Long tecnicoId, DisponibilidadRequestDTO request) {
        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new RuntimeException("Técnico no encontrado"));
        
        // Eliminar disponibilidades existentes
        disponibilidadRepository.findByTecnicoId(tecnicoId).forEach(disponibilidadRepository::delete);
        
        // Crear nuevas disponibilidades
        if (request.getDisponibilidades() != null) {
            for (DisponibilidadRequestDTO.DisponibilidadDTO dto : request.getDisponibilidades()) {
                DisponibilidadTecnico disponibilidad = DisponibilidadTecnico.builder()
                        .tecnico(tecnico)
                        .diaSemana(DisponibilidadTecnico.DiaSemana.valueOf(dto.getDiaSemana().toUpperCase()))
                        .horaInicio(LocalTime.parse(dto.getHoraInicio()))
                        .horaFin(LocalTime.parse(dto.getHoraFin()))
                        .disponible(dto.getDisponible())
                        .build();
                
                tecnico.getDisponibilidades().add(disponibilidad);
            }
        }
        
        tecnicoRepository.save(tecnico);
    }
}

