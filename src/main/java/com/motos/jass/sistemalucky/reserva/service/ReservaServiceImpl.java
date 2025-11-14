package com.motos.jass.sistemalucky.reserva.service;

import com.motos.jass.sistemalucky.auth.jwt.JwtUtil;
import com.motos.jass.sistemalucky.moto.entity.Moto;
import com.motos.jass.sistemalucky.moto.repository.MotoRepository;
import com.motos.jass.sistemalucky.reserva.dto.ReservaRequestDTO;
import com.motos.jass.sistemalucky.reserva.dto.ReservaResponseDTO;
import com.motos.jass.sistemalucky.reserva.entity.Reserva;
import com.motos.jass.sistemalucky.reserva.mapper.ReservaMapper;
import com.motos.jass.sistemalucky.reserva.repository.ReservaRepository;
import com.motos.jass.sistemalucky.share.service.BaseServiceImpl;
import com.motos.jass.sistemalucky.socio.entity.Socio;
import com.motos.jass.sistemalucky.socio.repository.SocioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class ReservaServiceImpl extends BaseServiceImpl  <Reserva, Long>
        implements ReservaService {


    private ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final SocioRepository socioRepository;
    private final MotoRepository motoRepository;
    private final JwtUtil jwtUtil;



    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              ReservaMapper reservaMapper,
                              SocioRepository socioRepository,
                              MotoRepository motoRepository, JwtUtil jwtUtil) {
        super(reservaRepository);
        this.reservaRepository = reservaRepository;
        this.reservaMapper = reservaMapper;
        this.socioRepository = socioRepository;
        this.motoRepository = motoRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public ReservaResponseDTO crearReserva(ReservaRequestDTO request, String token) {

        // 1️⃣ Obtener socio autenticado desde JWT
        String correoSocio = jwtUtil.extractUsername(token);
        Socio socioLogueado = socioRepository.findByCorreo(correoSocio)
                .orElseThrow(() -> new RuntimeException("Socio autenticado no encontrado"));

        // 2️⃣ Convertir DTO a entidad
        Reserva reserva = reservaMapper.toEntity(request);

        // 3️⃣ Asignar socio que registra (usando solo el ID)
        Socio socioRegistro = new Socio();
        socioRegistro.setId(socioLogueado.getId());
        reserva.setSocioRegistroRes(socioRegistro);

        // 4️⃣ Asignar el socio de la reserva (si viene en el request)
        if (request.getSocioId() != null) {
            Socio socioReserva = socioRepository.findById(request.getSocioId())
                    .orElseThrow(() -> new RuntimeException("Socio para la reserva no encontrado"));
            reserva.setSocio(socioReserva);
        }

        // 5️⃣ Asignar la moto de la reserva (para que la placa no salga null)
        if (request.getMotoId() != null) {
            Moto motoReserva = motoRepository.findById(request.getMotoId())
                    .orElseThrow(() -> new RuntimeException("Moto para la reserva no encontrada"));
            reserva.setMoto(motoReserva);
        }

        // 6️⃣ Asignar fecha actual si no vino
        if (reserva.getFechaReserva() == null) {
            reserva.setFechaReserva(LocalDate.now());
        }

        // 7️⃣ Guardar
        reservaRepository.save(reserva);

        // 8️⃣ Recargar para que tenga las relaciones actualizadas
        Reserva reservaGuardada = reservaRepository.findById(reserva.getId())
                .orElseThrow(() -> new RuntimeException("No se pudo recuperar la reserva guardada"));

        // 9️⃣ Devolver DTO completo
        return reservaMapper.toResponseDTO(reservaGuardada);
    }





}
