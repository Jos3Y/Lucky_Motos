package com.motos.jass.sistemalucky.vivienda.dto;

import lombok.Data;

@Data
public class ViviendaRequestDTO {
    private String direccion;
    private String numeroLote;
    private String manzana;
    private String nroFamilias;
    private long sectorId;
}
