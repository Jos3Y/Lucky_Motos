package com.motos.jass.sistemalucky.reportes.service;

import com.motos.jass.sistemalucky.cita.repository.CitaRepository;
import com.motos.jass.sistemalucky.cita.repository.CitaRepuestoRepository;
import com.motos.jass.sistemalucky.reportes.dto.ReportesResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportesServiceImpl implements ReportesService {

    private final CitaRepository citaRepository;
    private final CitaRepuestoRepository citaRepuestoRepository;

    @Override
    public ReportesResponseDTO obtenerResumen(String periodo) {
        LocalDate fin = LocalDate.now();
        LocalDate inicio = calcularInicio(periodo, fin);

        ReportesResponseDTO dto = new ReportesResponseDTO();
        dto.setMejoresTecnicos(mapearTecnicos(citaRepository.contarCitasPorTecnico(inicio, fin)));
        dto.setDiasConMasCitas(mapearDias(citaRepository.contarCitasPorDia(inicio, fin)));
        dto.setRepuestosMasUsados(mapearRepuestos(citaRepuestoRepository.contarRepuestosUtilizados(inicio, fin)));
        return dto;
    }

    @Override
    public ByteArrayInputStream generarExcel(String periodo) {
        ReportesResponseDTO datos = obtenerResumen(periodo);
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            crearHojaTecnicos(workbook.createSheet("Mejores técnicos"), datos.getMejoresTecnicos());
            crearHojaDias(workbook.createSheet("Días con más citas"), datos.getDiasConMasCitas());
            crearHojaRepuestos(workbook.createSheet("Repuestos más usados"), datos.getRepuestosMasUsados());
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("No se pudo generar el Excel", e);
        }
    }

    private void crearHojaTecnicos(XSSFSheet sheet, List<ReportesResponseDTO.ReporteTecnicoDTO> data) {
        String[] headers = {"Técnico", "Total citas"};
        crearEncabezado(sheet, headers);
        int rowIdx = 1;
        for (ReportesResponseDTO.ReporteTecnicoDTO item : data) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(item.getNombreCompleto());
            row.createCell(1).setCellValue(item.getTotalCitas());
        }
    }

    private void crearHojaDias(XSSFSheet sheet, List<ReportesResponseDTO.ReporteDiaDTO> data) {
        String[] headers = {"Fecha", "Total citas"};
        crearEncabezado(sheet, headers);
        int rowIdx = 1;
        for (ReportesResponseDTO.ReporteDiaDTO item : data) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(item.getFecha());
            row.createCell(1).setCellValue(item.getTotalCitas());
        }
    }

    private void crearHojaRepuestos(XSSFSheet sheet, List<ReportesResponseDTO.ReporteRepuestoDTO> data) {
        String[] headers = {"Repuesto", "Cantidad usada"};
        crearEncabezado(sheet, headers);
        int rowIdx = 1;
        for (ReportesResponseDTO.ReporteRepuestoDTO item : data) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(item.getNombreRepuesto());
            row.createCell(1).setCellValue(item.getCantidad());
        }
    }

    private void crearEncabezado(XSSFSheet sheet, String[] headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            sheet.autoSizeColumn(i);
            cell.setCellValue(headers[i]);
        }
    }

    private LocalDate calcularInicio(String periodo, LocalDate fin) {
        return switch (periodo == null ? "" : periodo.toUpperCase()) {
            case "ULTIMOS_3_MESES" -> fin.minusMonths(3);
            case "ULTIMO_MES" -> fin.minusMonths(1);
            default -> fin.minusMonths(6);
        };
    }

    private List<ReportesResponseDTO.ReporteTecnicoDTO> mapearTecnicos(List<Object[]> data) {
        return data.stream().limit(10).map(obj -> {
            ReportesResponseDTO.ReporteTecnicoDTO dto = new ReportesResponseDTO.ReporteTecnicoDTO();
            dto.setTecnicoId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
            String nombre = (String) obj[1];
            String apellidos = (String) obj[2];
            dto.setNombreCompleto(String.format("%s %s", nombre != null ? nombre : "", apellidos != null ? apellidos : "").trim());
            dto.setTotalCitas(obj[3] != null ? ((Number) obj[3]).longValue() : 0L);
            return dto;
        }).toList();
    }

    private List<ReportesResponseDTO.ReporteDiaDTO> mapearDias(List<Object[]> data) {
        return data.stream().limit(10).map(obj -> {
            ReportesResponseDTO.ReporteDiaDTO dto = new ReportesResponseDTO.ReporteDiaDTO();
            dto.setFecha(obj[0] != null ? obj[0].toString() : "");
            dto.setTotalCitas(obj[1] != null ? ((Number) obj[1]).longValue() : 0L);
            return dto;
        }).toList();
    }

    private List<ReportesResponseDTO.ReporteRepuestoDTO> mapearRepuestos(List<Object[]> data) {
        return data.stream().limit(10).map(obj -> {
            ReportesResponseDTO.ReporteRepuestoDTO dto = new ReportesResponseDTO.ReporteRepuestoDTO();
            dto.setNombreRepuesto(obj[0] != null ? obj[0].toString() : "");
            dto.setCantidad(obj[1] != null ? ((Number) obj[1]).longValue() : 0L);
            return dto;
        }).toList();
    }
}

