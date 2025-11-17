package com.motos.jass.sistemalucky.auth.jwt;

import com.motos.jass.sistemalucky.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        final String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                // Convertir roles a GrantedAuthority con prefijo ROLE_
                Set<String> roles = jwtUtil.extractRoles(token);
                System.out.println("🔐 Roles extraídos del token: " + roles);
                var authorities = roles.stream()
                        .map(role -> {
                            // Normalizar: remover ROLE_ si existe, luego agregarlo
                            String normalizedRole = role.toUpperCase().trim();
                            // Remover prefijo ROLE_ si ya existe
                            if (normalizedRole.startsWith("ROLE_")) {
                                normalizedRole = normalizedRole.substring(5); // Remover "ROLE_"
                            }
                            // Agregar prefijo ROLE_ (Spring Security lo requiere)
                            normalizedRole = "ROLE_" + normalizedRole;
                            System.out.println("🔐 Rol normalizado: " + normalizedRole);
                            return new SimpleGrantedAuthority(normalizedRole);
                        })
                        .collect(Collectors.toList());
                
                System.out.println("🔐 Authorities creadas: " + authorities);

                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Ignorar endpoints públicos
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        System.out.println("Request path: " + path);

        // Ignora todos los endpoints públicos
        return path.startsWith("/auth/") || path.startsWith("/api/auth/");
    }

}
