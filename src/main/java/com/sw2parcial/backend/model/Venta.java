package com.sw2parcial.backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "venta_total", nullable = false)
    private Float ventaTotal;

    @Column(name = "fecha_venta", nullable = false)
    private LocalDate fechaVenta;

    private String estado;

    @Column(name = "canal_venta")
    private String canalVenta;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DetalleVenta> detalleVentas;

    // Constructores
    public Venta() {
    }

    public Venta(Float ventaTotal, LocalDate fechaVenta, String estado, String canalVenta, Usuario usuario) {
        this.ventaTotal = ventaTotal;
        this.fechaVenta = fechaVenta;
        this.estado = estado;
        this.canalVenta = canalVenta;
        this.usuario = usuario;
    }

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(Float ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleVenta> getDetalleVentas() {
        return detalleVentas;
    }

    public void setDetalleVentas(List<DetalleVenta> detalleVentas) {
        this.detalleVentas = detalleVentas;
    }
}