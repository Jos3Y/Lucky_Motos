package com.motos.jass.sistemalucky.repuesto.service;

import com.motos.jass.sistemalucky.repuesto.dto.RepuestoRequestDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoResponseDTO;
import com.motos.jass.sistemalucky.repuesto.dto.RepuestoFilterDTO;
import com.motos.jass.sistemalucky.repuesto.entity.Repuesto;
import com.motos.jass.sistemalucky.repuesto.mapper.RepuestoMapper;
import com.motos.jass.sistemalucky.repuesto.repository.RepuestoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepuestoServiceImpl implements RepuestoService {
    
    private final RepuestoRepository repuestoRepository;
    private final RepuestoMapper repuestoMapper;
    
    @Override
    @Transactional
    public RepuestoResponseDTO crearRepuesto(RepuestoRequestDTO request) {
        Repuesto repuesto = repuestoMapper.toEntity(request);
        Repuesto repuestoGuardado = repuestoRepository.save(repuesto);
        return repuestoMapper.toResponseDTO(repuestoGuardado);
    }
    
    @Override
    public RepuestoResponseDTO obtenerRepuestoPorId(Long id) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        return repuestoMapper.toResponseDTO(repuesto);
    }
    
    @Override
    public List<RepuestoResponseDTO> obtenerTodosLosRepuestos() {
        List<Repuesto> todos = repuestoRepository.findAll();
        System.out.println("DEBUG - Total de repuestos en BD: " + todos.size());
        return todos.stream()
                .map(repuestoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RepuestoResponseDTO> filtrarRepuestos(RepuestoFilterDTO filter) {
        List<Repuesto> repuestos;

        boolean hasMarca = filter.getMarca() != null && !filter.getMarca().isEmpty();
        boolean hasModelo = filter.getModelo() != null && !filter.getModelo().isEmpty();
        boolean hasRepuesto = filter.getRepuesto() != null && !filter.getRepuesto().isEmpty();

        if (hasMarca && hasModelo) {
            repuestos = repuestoRepository.findByMarcaAndModelo(filter.getMarca(), filter.getModelo());
        } else if (hasMarca) {
            repuestos = repuestoRepository.findByMarca(filter.getMarca());
        } else if (hasModelo) {
            repuestos = repuestoRepository.findByModeloCompatible(filter.getModelo());
        } else if (hasRepuesto) {
            repuestos = repuestoRepository.findByNombreContainingIgnoreCase(filter.getRepuesto());
        }
        else {
            repuestos = repuestoRepository.findAll();
        }

        return repuestos.stream()
                .map(repuestoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public RepuestoResponseDTO actualizarRepuesto(Long id, RepuestoRequestDTO request) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        
        if (request.getNombre() != null) repuesto.setNombre(request.getNombre());
        if (request.getMarca() != null) repuesto.setMarca(request.getMarca());
        if (request.getModeloCompatible() != null) repuesto.setModeloCompatible(request.getModeloCompatible());
        if (request.getStock() != null) repuesto.setStock(request.getStock());
        if (request.getPrecio() != null) repuesto.setPrecio(request.getPrecio());
        
        Repuesto repuestoActualizado = repuestoRepository.save(repuesto);
        return repuestoMapper.toResponseDTO(repuestoActualizado);
    }
    
    @Override
    @Transactional
    public void eliminarRepuesto(Long id) {
        Repuesto repuesto = repuestoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repuesto no encontrado"));
        repuestoRepository.delete(repuesto);
    }
    
    @Override
    public List<RepuestoResponseDTO> obtenerRepuestosBajoStock() {
        return repuestoRepository.findBajoStock().stream()
                .map(repuestoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

