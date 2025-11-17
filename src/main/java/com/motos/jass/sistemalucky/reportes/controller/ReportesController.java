package com.motos.jass.sistemalucky.reportes.controller;

import com.motos.jass.sistemalucky.reportes.dto.ReportesResponseDTO;
import com.motos.jass.sistemalucky.reportes.service.ReportesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final ReportesService reportesService;

    @GetMapping("/resumen")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<ReportesResponseDTO> obtenerResumen(
            @RequestParam(defaultValue = "ULTIMOS_6_MESES") String periodo) {
        return ResponseEntity.ok(reportesService.obtenerResumen(periodo));
    }

    @GetMapping("/export/excel")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
    public ResponseEntity<byte[]> exportarExcel(
            @RequestParam(defaultValue = "ULTIMOS_6_MESES") String periodo) {
        ByteArrayInputStream stream = reportesService.generarExcel(periodo);
        byte[] bytes = stream.readAllBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reportes.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }
}

