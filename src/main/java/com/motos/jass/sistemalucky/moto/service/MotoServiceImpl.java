package com.motos.jass.sistemalucky.moto.service;

import com.motos.jass.sistemalucky.moto.dto.MotoRequestDTO;
import com.motos.jass.sistemalucky.moto.dto.MotoResponseDTO;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.moto.mapper.MotoMapper;
import com.motos.jass.sistemalucky.moto.repository.MotoRepository;
import com.motos.jass.sistemalucky.reserva.repository.ReservaRepository;
import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class MotoServiceImpl extends BaseServiceImpl<Moto, Long> implements MotoService {

    private MotoRepository motoRepository;

    private final MotoMapper motoMapper;

    private final SocioRepository socioRepository;


    MotoServiceImpl (MotoRepository motoRepository, MotoMapper motoMapper, SocioRepository socioRepository ) {
        super(motoRepository);
        this.motoRepository = motoRepository;
        this.motoMapper = motoMapper;
        this.socioRepository = socioRepository;

    }

    @Transactional
    public MotoResponseDTO crearMoto(MotoRequestDTO request) {
        System.out.println("Entrando a la peticion RolRequestDTO"+request.toString());
        // Buscar el socio
        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        // Convertir DTO a entidad con MapStruct
        Moto moto = motoMapper.toEntity(request);

        // Asignar el socio y las fechas que no maneja MapStruct
        moto.setSocio(socio);

        // Guardar
        motoRepository.save(moto);

        // Devolver DTO de respuesta
        return motoMapper.toResponseDTO(moto);
    }

    @Transactional
    public MotoResponseDTO actualizarMoto(Long id, MotoRequestDTO request) {
        System.out.println("Entrando a actualizar moto ID: " + id);

        // 1 Buscar la moto existente
        Moto motoExistente = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto no encontrada con id: " + id));

        // 2 Buscar el socio nuevo (si cambia)
        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con id: " + request.getSocioId()));

        // 3 Actualizar los campos manualmente (para mantener las fechas y evitar sobrescribir el ID)
        motoExistente.setPlaca(request.getPlaca());
        motoExistente.setModelo(request.getModelo());
        motoExistente.setMarca(request.getMarca());
        motoExistente.setTipoCombustible(request.getTipoCombustible());
        motoExistente.setNroSerieMotor(request.getNroSerieMotor());
        motoExistente.setNumeroChasis(request.getNumeroChasis());
        motoExistente.setKilometrajeActual(request.getKilometrajeActual());
        motoExistente.setSocio(socio);

        // 4 Guardar cambios
        motoRepository.save(motoExistente);

        // 5 Devolver la respuesta en formato DTO
        return motoMapper.toResponseDTO(motoExistente);
    }


    @Transactional(readOnly = true)
    public List<MotoResponseDTO> listarMotos() {
        // Obtener todas las motos desde el repositorio
        List<Moto> motos = motoRepository.findAll();

        // Convertir cada entidad a DTO con el mapper
        return motos.stream()
                .map(motoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public MotoResponseDTO eliminarMoto(Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Moto no encontrada con id: " + id));

        moto.setEstado("ELIMINADA");
        motoRepository.save(moto);

        return motoMapper.toResponseDTO(moto);
    }

    public List<MotoResponseDTO> listarActivas() {
        return motoRepository.findAllActivas()
                .stream()
                .map(motoMapper::toResponseDTO)
                .toList();
    }



}
