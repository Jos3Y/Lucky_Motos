package com.motos.jass.sistemalucky.auth.service;

import com.motos.jass.sistemalucky.auth.dto.RegisterRequestDTO;
import com.motos.jass.sistemalucky.auth.dto.RegisterResponseDTO;
import com.motos.jass.sistemalucky.auth.mapper.RegisterMapper;
import com.motos.jass.sistemalucky.socio.entity.Rol;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.entity.RolSocio;
import com.motos.jass.sistemalucky.socio.repository.RolRepository;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class RegisterServiceImple {

    private final SocioRepository socioRepository;
    private final RegisterMapper registerMapper;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;


    @Transactional
    public RegisterResponseDTO registrarNuevoSocio(RegisterRequestDTO request) {
        System.out.println("Entrando a la peticion: "+ request);
        // 1. Verificar si ya existe un socio con ese correo
        if (socioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        // 2. Mapear DTO a entidad
        Socio socio = registerMapper.toEntity(request);

        // 3. Codificar contraseña
        socio.setContrasena(passwordEncoder.encode(request.getContrasena()));
        socio.setFechaRegistro(LocalDate.now());
        socio.setEstado(true); // 👈 Ahora el socio también tiene estado activo
        System.out.println("contraseña: "+socio.getContrasena());

        // 5. Asignar roles (por defecto o desde lista)
        // Suponiendo que el rol por defecto tiene ID =     4
        Rol rolPorDefecto = rolRepository.findById(4L)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        String descripcion1 = rolPorDefecto.getDescripcion();
        System.out.println("Rol asignado:"+ descripcion1);

        RolSocio relacion = new RolSocio();
        relacion.setSocio(socio);
        relacion.setRol(rolPorDefecto);
        relacion.setDescripcion("Usuario Recien Registrado");
        relacion.setEstado("ACTIVO");

        if (socio.getRolSocios() == null) {
            socio.setRolSocios(new ArrayList<>());
        }
        socio.getRolSocios().add(relacion);

        // Guardar la relación intermedia si no es en cascade
        Socio socioGuardado = socioRepository.save(socio);

        // 6. Retornar DTO de respuesta
        return registerMapper.toResponseDTO(socioGuardado);
    }
}
