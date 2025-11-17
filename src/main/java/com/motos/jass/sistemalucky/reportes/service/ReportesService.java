package com.motos.jass.sistemalucky.reportes.service;

import com.motos.jass.sistemalucky.reportes.dto.ReportesResponseDTO;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;

public interface ReportesService {
    ReportesResponseDTO obtenerResumen(String periodo);
    ByteArrayInputStream generarExcel(String periodo);
}

