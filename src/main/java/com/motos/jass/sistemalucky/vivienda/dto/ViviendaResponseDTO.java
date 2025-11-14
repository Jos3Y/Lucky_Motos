package com.motos.jass.sistemalucky.vivienda.dto;

import lombok.Data;

@Data
public class ViviendaResponseDTO {

    private long sectorId;
    private String direccion;
    private String numeroLote;
    private String manzana;
    private int nroFamilias;
    private boolean estado;

    private String nombreSector;



}
