package com.motos.jass.sistemalucky.moto.service;

import com.motos.jass.sistemalucky.cliente.entity.Cliente;
import com.motos.jass.sistemalucky.cliente.repository.ClienteRepository;
import com.motos.jass.sistemalucky.moto.dto.MotoRequestDTO;
import com.motos.jass.sistemalucky.moto.dto.MotoResponseDTO;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.moto.mapper.MotoMapper;
import com.motos.jass.sistemalucky.moto.repository.MotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MotoServiceImpl implements MotoService {

    private final MotoRepository motoRepository;

    private final MotoMapper motoMapper;

    private final ClienteRepository clienteRepository;


    MotoServiceImpl (MotoRepository motoRepository, MotoMapper motoMapper, ClienteRepository clienteRepository ) {
        this.motoRepository = motoRepository;
        this.motoMapper = motoMapper;
        this.clienteRepository = clienteRepository;

    }

    @Override
    public List<Moto> findAll() {
        return motoRepository.findAll();
    }

    @Override
    public Optional<Moto> findById(Long id) {
        return motoRepository.findById(id);
    }

    @Override
    public Moto save(Moto entity) {
        return motoRepository.save(entity);
    }

    @Override
    public Moto update(Long id, Moto entity) {
        if(!motoRepository.existsById(id)) {
            throw new RuntimeException("No existe el registro con ID: " + id);
        }
        return motoRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        if (!motoRepository.existsById(id)) {
            throw new RuntimeException("No existe el registro con ID: " + id);
        }
        motoRepository.deleteById(id);
    }

    @Transactional
    public MotoResponseDTO crearMoto(MotoRequestDTO request) {
        System.out.println("Entrando a la peticion MotoRequestDTO"+request.toString());
        // Buscar el cliente
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Convertir DTO a entidad con MapStruct
        Moto moto = motoMapper.toEntity(request);

        // Asignar el cliente y las fechas que no maneja MapStruct
        moto.setCliente(cliente);
        moto.setEstado(Moto.EstadoMoto.ACTIVO);

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

        // 2 Buscar el cliente nuevo (si cambia)
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + request.getClienteId()));

        // 3 Actualizar los campos manualmente (para mantener las fechas y evitar sobrescribir el ID)
        motoExistente.setPlaca(request.getPlaca());
        motoExistente.setModelo(request.getModelo());
        motoExistente.setMarca(request.getMarca());
        motoExistente.setAnio(request.getAnio());
        motoExistente.setTipoCombustible(request.getTipoCombustible());
        motoExistente.setNroSerieMotor(request.getNroSerieMotor());
        motoExistente.setNumeroChasis(request.getNumeroChasis());
        if (request.getKilometrajeActual() != null) {
            motoExistente.setKilometrajeActual(request.getKilometrajeActual());
        }
        motoExistente.setCliente(cliente);

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

        moto.setEstado(Moto.EstadoMoto.INACTIVO);
        motoRepository.save(moto);

        return motoMapper.toResponseDTO(moto);
    }

    public List<MotoResponseDTO> listarActivas() {
        return motoRepository.findAllActivas()
                .stream()
                .map(motoMapper::toResponseDTO)
                .toList();
    }

    public List<String> obtenerModelosUnicos() {
        return motoRepository.findDistinctModelos();
    }

    public MotoResponseDTO obtenerMotoPorModelo(String modelo) {
        Moto moto = motoRepository.findFirstByModeloAndEstado(modelo, Moto.EstadoMoto.ACTIVO)
                .orElseThrow(() -> new RuntimeException("No se encontró una moto con el modelo: " + modelo));
        return motoMapper.toResponseDTO(moto);
    }

}
