package com.motos.jass.sistemalucky.tecnico.service;

import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import com.motos.jass.sistemalucky.tecnico.dto.DisponibilidadRequestDTO;
import com.motos.jass.sistemalucky.tecnico.dto.TecnicoRequestDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
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
        Objects.requireNonNull(id, "El id del técnico es obligatorio");
        long tecnicoIdValue = Objects.requireNonNull(id, "El id del técnico es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findById(tecnicoIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
        return tecnicoMapper.toResponseDTO(tecnico);
    }
    
    @Override
    public TecnicoResponseDTO obtenerTecnicoPorSocioId(Long socioId) {
        Objects.requireNonNull(socioId, "El socio es obligatorio");
        long socioIdValue = Objects.requireNonNull(socioId, "El socio es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findBySocioId(socioIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
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
        Objects.requireNonNull(tecnicoId, "El id del técnico es obligatorio");
        long tecnicoIdValue = Objects.requireNonNull(tecnicoId, "El id del técnico es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findById(tecnicoIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
        
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

    @Override
    @Transactional
    @SuppressWarnings({"ConstantConditions", "DataFlowIssue"})
    public TecnicoResponseDTO crearTecnico(TecnicoRequestDTO request) {
        long socioId = Objects.requireNonNull(request.getSocioId(), "El socio es obligatorio");
        if (tecnicoRepository.existsBySocioId(socioId)) {
            throw new RuntimeException("El socio ya está asignado a un técnico");
        }
        Socio socio = Objects.requireNonNull(
                socioRepository.findById(socioId)
                        .orElseThrow(() -> new RuntimeException("Socio no encontrado"))
        );

        if (request.getNombre() != null) socio.setNombre(request.getNombre());
        if (request.getApellidos() != null) socio.setApellidos(request.getApellidos());
        if (request.getCorreo() != null) socio.setCorreo(request.getCorreo());
        if (request.getTelefono() != null) socio.setTelefono(request.getTelefono());
        
        Tecnico tecnico = Tecnico.builder()
                .socio(socio)
                .especialidad(request.getEspecialidad())
                .estado(parseEstado(request.getEstado()))
                .build();
        
        aplicarHorarioDefault(tecnico);
        Tecnico guardado = tecnicoRepository.save(tecnico);
        TecnicoResponseDTO dto = tecnicoMapper.toResponseDTO(guardado);
        return Objects.requireNonNull(dto, "No se pudo registrar el técnico");
    }

    @Override
    @Transactional
    @SuppressWarnings({"ConstantConditions", "DataFlowIssue"})
    public TecnicoResponseDTO actualizarTecnico(Long id, TecnicoRequestDTO request) {
        long tecnicoIdValue = Objects.requireNonNull(id, "El id del técnico es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findById(tecnicoIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
        if (request.getEspecialidad() != null) {
            tecnico.setEspecialidad(request.getEspecialidad());
        }
        if (request.getNombre() != null) {
            tecnico.getSocio().setNombre(request.getNombre());
        }
        if (request.getApellidos() != null) {
            tecnico.getSocio().setApellidos(request.getApellidos());
        }
        if (request.getCorreo() != null) {
            tecnico.getSocio().setCorreo(request.getCorreo());
        }
        if (request.getTelefono() != null) {
            tecnico.getSocio().setTelefono(request.getTelefono());
        }
        if (request.getEstado() != null) {
            tecnico.setEstado(parseEstado(request.getEstado()));
        }
        Tecnico actualizado = tecnicoRepository.save(tecnico);
        TecnicoResponseDTO dto = tecnicoMapper.toResponseDTO(actualizado);
        return Objects.requireNonNull(dto, "No se pudo actualizar el técnico");
    }

    @Override
    @Transactional
    public void cambiarEstado(Long id, String estado) {
        long tecnicoIdValue = Objects.requireNonNull(id, "El id del técnico es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findById(tecnicoIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
        tecnico.setEstado(parseEstado(estado));
        tecnicoRepository.save(tecnico);
    }

    @Override
    @Transactional
    public void eliminarTecnico(Long id) {
        long tecnicoIdValue = Objects.requireNonNull(id, "El id del técnico es obligatorio");
        Tecnico tecnico = Objects.requireNonNull(
                tecnicoRepository.findById(tecnicoIdValue)
                        .orElseThrow(() -> new RuntimeException("Técnico no encontrado"))
        );
        tecnico.setEstado(Tecnico.EstadoTecnico.INACTIVO);
        tecnico.getDisponibilidades().clear();
        tecnicoRepository.save(tecnico);
    }

    @Override
    @Transactional
    public void resetearHorariosDefault() {
        List<Tecnico> lista = tecnicoRepository.findAll();
        for (Tecnico tecnico : lista) {
            if (tecnico == null) continue;
            aplicarHorarioDefault(tecnico);
            tecnicoRepository.save(tecnico);
        }
    }

    private void aplicarHorarioDefault(Tecnico tecnico) {
        tecnico.getDisponibilidades().clear();
        for (DisponibilidadTecnico.DiaSemana dia : DisponibilidadTecnico.DiaSemana.values()) {
            if (dia == DisponibilidadTecnico.DiaSemana.DOMINGO) {
                continue;
            }
            DisponibilidadTecnico disponibilidad = DisponibilidadTecnico.builder()
                    .tecnico(tecnico)
                    .diaSemana(dia)
                    .horaInicio(LocalTime.of(8, 0))
                    .horaFin(LocalTime.of(20, 0))
                    .disponible(true)
                    .build();
            tecnico.getDisponibilidades().add(disponibilidad);
        }
    }

    private Tecnico.EstadoTecnico parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Tecnico.EstadoTecnico.DISPONIBLE;
        }
        try {
            return Tecnico.EstadoTecnico.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Estado de técnico inválido");
        }
    }
}

