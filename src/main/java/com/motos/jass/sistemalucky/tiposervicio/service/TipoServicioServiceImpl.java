package com.motos.jass.sistemalucky.tiposervicio.service;

import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioRequestDTO;
import com.motos.jass.sistemalucky.tiposervicio.dto.TipoServicioResponseDTO;
import com.motos.jass.sistemalucky.tiposervicio.entity.TipoServicio;
import com.motos.jass.sistemalucky.tiposervicio.mapper.TipoServicioMapper;
import com.motos.jass.sistemalucky.tiposervicio.repository.TipoServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoServicioServiceImpl implements TipoServicioService {

    private final TipoServicioRepository tipoServicioRepository;
    private final TipoServicioMapper tipoServicioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TipoServicioResponseDTO> listarTodos() {
        return tipoServicioRepository.findAll()
                .stream()
                .map(tipoServicioMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoServicioResponseDTO obtenerPorId(Long id) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));
        return tipoServicioMapper.toDto(tipoServicio);
    }

    @Override
    @Transactional
    public TipoServicioResponseDTO crear(TipoServicioRequestDTO request) {
        TipoServicio tipoServicio = tipoServicioMapper.toEntity(request);
        return tipoServicioMapper.toDto(tipoServicioRepository.save(tipoServicio));
    }

    @Override
    @Transactional
    public TipoServicioResponseDTO actualizar(Long id, TipoServicioRequestDTO request) {
        TipoServicio tipoServicio = tipoServicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de servicio no encontrado"));

        if (request.getNombre() != null) {
            tipoServicio.setNombre(request.getNombre());
        }
        tipoServicio.setDescripcion(request.getDescripcion());
        if (request.getPrecioBase() != null) {
            tipoServicio.setPrecioBase(request.getPrecioBase());
        }
        tipoServicio.setDuracionEstimadaMinutos(request.getDuracionEstimadaMinutos());

        return tipoServicioMapper.toDto(tipoServicioRepository.save(tipoServicio));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!tipoServicioRepository.existsById(id)) {
            throw new RuntimeException("Tipo de servicio no encontrado");
        }
        tipoServicioRepository.deleteById(id);
    }
}

