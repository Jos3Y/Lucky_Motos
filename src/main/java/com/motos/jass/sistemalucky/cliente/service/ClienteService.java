package com.motos.jass.sistemalucky.cliente.service;

import com.motos.jass.sistemalucky.cliente.dto.ClienteRequestDTO;
import com.motos.jass.sistemalucky.cliente.dto.ClienteResponseDTO;
import com.motos.jass.sistemalucky.cliente.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<ClienteResponseDTO> obtenerTodos();
    Optional<ClienteResponseDTO> obtenerPorId(Long id);
    ClienteResponseDTO crearCliente(ClienteRequestDTO request);
    ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO request);
    void eliminarCliente(Long id);
    Optional<Cliente> buscarPorDni(String dni);
    Optional<Cliente> buscarPorCorreo(String correo);
}

