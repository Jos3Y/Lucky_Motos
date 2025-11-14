    package com.motos.jass.sistemalucky.config;

    import com.motos.jass.sistemalucky.auth.jwt.JwtAuthenticationFilter;
    import com.motos.jass.sistemalucky.auth.service.CustomUserDetailsService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true) //
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final CustomUserDetailsService customUserDetailsService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
            http
                    // Sin sesión, todo JWT
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // Desactivar CSRF (API REST)
                    .csrf(csrf -> csrf.disable())

                    // Configuración de rutas
                    .authorizeHttpRequests(auth -> auth
                            // Vistas públicas y recursos estáticos de React
                            .requestMatchers("/", "/login", "/register", "/index.html", "/assets/**", "/images/**", "/css/**", "/js/**").permitAll()
                            // Consola H2 (solo para desarrollo)
                            .requestMatchers("/h2-console/**").permitAll()
                            // Endpoints públicos
                            .requestMatchers("/auth/**").permitAll()

                            // Rutas de API con roles específicos
                            .requestMatchers("/api/citas/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "TECNICO", "CLIENTE")
                            .requestMatchers("/api/repuestos/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "TECNICO")
                            .requestMatchers("/api/tecnicos/**").hasAnyRole("ADMIN", "RECEPCIONISTA", "TECNICO")
                            
                            // Rutas legacy (mantener compatibilidad)
                            .requestMatchers("/socio/**", "/rol/**", "/rol-socio/**", "/moto/**", "/reserva/**").hasAnyRole("SOCIO", "ADMIN", "CLIENTE")

                            // Rutas exclusivas de ADMIN
                            .requestMatchers("/admin/**").hasRole("ADMIN")

                            // Cualquier otra ruta requiere autenticación
                            .anyRequest().authenticated()
                    )
                    
                    // Permitir frames para H2 console
                    .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

                    // 🔹 Agregar el filtro JWT antes del filtro de autenticación por usuario/contraseña
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }

        // Configurar AuthenticationManager con tu servicio de usuarios y encoder
        @Bean
            public AuthenticationManager authManager(HttpSecurity http) throws Exception {
            AuthenticationManagerBuilder authBuilder =
                    http.getSharedObject(AuthenticationManagerBuilder.class);

            authBuilder
                    .userDetailsService(customUserDetailsService)
                    .passwordEncoder(passwordEncoder());

            return authBuilder.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {

            return new BCryptPasswordEncoder();
        }
    }


