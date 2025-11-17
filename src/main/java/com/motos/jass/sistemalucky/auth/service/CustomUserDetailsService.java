package com.motos.jass.sistemalucky.auth.service;

import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SocioRepository socioRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Socio socio = socioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String[] roles = socio.getRolSocios().stream()
                .filter(rolSocio -> {
                    String estado = rolSocio.getEstado() != null ? rolSocio.getEstado() : "";
                    return "ACTIVO".equalsIgnoreCase(estado) || "Activo".equalsIgnoreCase(estado);
                })
                .map(rolSocio -> {
                    // Normalizar roles: remover prefijo ROLE_ si existe
                    String descripcion = rolSocio.getRol().getDescripcion();
                    if (descripcion != null && descripcion.startsWith("ROLE_")) {
                        return descripcion.substring(5); // Remover "ROLE_"
                    }
                    return descripcion != null ? descripcion : "";
                })
                .toArray(String[]::new);

        return User.builder()
                .username(socio.getCorreo())
                .password(socio.getContrasena())
                .roles(roles) // Spring Security añadirá automáticamente "ROLE_"
                .build();
    }

}