package com.motos.jass.sistemalucky.cliente.controller;

import com.motos.jass.sistemalucky.cliente.dto.ClienteRequestDTO;
import com.motos.jass.sistemalucky.cliente.dto.ClienteResponseDTO;
import com.motos.jass.sistemalucky.cliente.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<List<ClienteResponseDTO>> obtenerTodos() {
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA', 'TECNICO')")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO response = clienteService.crearCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
            @PathVariable Long id,
            @RequestBody ClienteRequestDTO request) {
        ClienteResponseDTO response = clienteService.actualizarCliente(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/buscar/dni/{dni}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClienteResponseDTO> buscarPorDni(@PathVariable String dni) {
        return clienteService.buscarPorDni(dni)
                .map(cliente -> {
                    ClienteResponseDTO dto = new ClienteResponseDTO();
                    dto.setId(cliente.getId());
                    dto.setNombre(cliente.getNombre());
                    dto.setApellidos(cliente.getApellidos());
                    dto.setDni(cliente.getDni());
                    dto.setCorreo(cliente.getCorreo());
                    dto.setTelefono(cliente.getTelefono());
                    dto.setDireccion(cliente.getDireccion());
                    dto.setEstado(cliente.getEstado());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

