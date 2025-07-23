package com.sw2parcial.backend.model;

public class ReporteVentaPorTipo {
    private String tipoNombre;
    private Long cantidadVentas;
    private Double totalGenerado;

    public ReporteVentaPorTipo(String tipoNombre, Long cantidadVentas, Double totalGenerado) {
        this.tipoNombre = tipoNombre;
        this.cantidadVentas = cantidadVentas;
        this.totalGenerado = totalGenerado;
    }

    public String getTipoNombre() { return tipoNombre; }
    public Long getCantidadVentas() { return cantidadVentas; }
    public Double getTotalGenerado() { return totalGenerado; }
}
