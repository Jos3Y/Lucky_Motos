package com.motos.jass.sistemalucky.cliente.service;

import com.motos.jass.sistemalucky.cliente.dto.ClienteRequestDTO;
import com.motos.jass.sistemalucky.cliente.dto.ClienteResponseDTO;
import com.motos.jass.sistemalucky.cliente.entity.Cliente;
import com.motos.jass.sistemalucky.cliente.mapper.ClienteMapper;
import com.motos.jass.sistemalucky.cliente.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    
    @Override
    public List<ClienteResponseDTO> obtenerTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ClienteResponseDTO> obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toResponseDTO);
    }
    
    @Override
    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteGuardado);
    }
    
    @Override
    @Transactional
    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setDni(request.getDni());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        
        Cliente clienteActualizado = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteActualizado);
    }
    
    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }
    
    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }
    
    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        return clienteRepository.findByCorreo(correo);
    }
}

