package com.sw2parcial.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class VentaRequestDTO {
    
    private Integer usuarioId;
    private String estado;
    private String canalVenta;
    private LocalDate fechaVenta;
    private Float ventaTotal;
    private List<DetalleVentaDTO> detalles;

    // Constructores
    public VentaRequestDTO() {
    }

    public VentaRequestDTO(Integer usuarioId, String estado, String canalVenta, LocalDate fechaVenta, List<DetalleVentaDTO> detalles) {
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.canalVenta = canalVenta;
        this.fechaVenta = fechaVenta;
        this.detalles = detalles;
    }

    // Getters y setters
    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCanalVenta() {
        return canalVenta;
    }

    public void setCanalVenta(String canalVenta) {
        this.canalVenta = canalVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }

    public Float getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(Float ventaTotal) {
        this.ventaTotal = ventaTotal;
    }


    // Clase interna para los detalles
    public static class DetalleVentaDTO {
        private Integer productoId;
        private Integer cantidad;
        private Float precioUnitario;

        // Constructores
        public DetalleVentaDTO() {
        }

        public DetalleVentaDTO(Integer productoId, Integer cantidad, Float precioUnitario) {
            this.productoId = productoId;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        // Getters y setters
        public Integer getProductoId() {
            return productoId;
        }

        public void setProductoId(Integer productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Float getPrecioUnitario() {
            return precioUnitario;
        }

        public void setPrecioUnitario(Float precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
}