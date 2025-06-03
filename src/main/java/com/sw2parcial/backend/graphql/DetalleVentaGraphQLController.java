package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.repository.DetalleVentaRepository;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DetalleVentaGraphQLController {

    @Autowired
    private DetalleVentaRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @QueryMapping
    public List<DetalleVenta> listarDetallesVenta() {
        return detalleRepository.findAll();
    }

    @QueryMapping
    public DetalleVenta obtenerDetalleVenta(@Argument Integer id) {
        return detalleRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<DetalleVenta> listarDetallesPorVenta(@Argument Integer ventaId) {
        return detalleRepository.findByVentaId(ventaId);
    }

    @MutationMapping
    public DetalleVenta crearDetalleVenta(
            @Argument Integer productoId,
            @Argument Integer cantidad,
            @Argument Float precioUnitario,
            @Argument Integer ventaId
    ) {
        Producto producto = productoRepository.findById(productoId).orElseThrow();
        Venta venta = ventaRepository.findById(ventaId).orElseThrow();
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setVenta(venta);
        return detalleRepository.save(detalle);
    }

    @MutationMapping
    public DetalleVenta actualizarDetalleVenta(
            @Argument Integer id,
            @Argument Integer productoId,
            @Argument Integer cantidad,
            @Argument Float precioUnitario,
            @Argument Integer ventaId
    ) {
        DetalleVenta detalle = detalleRepository.findById(id).orElse(null);
        if (detalle == null) return null;
        detalle.setProducto(productoRepository.findById(productoId).orElseThrow());
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setVenta(ventaRepository.findById(ventaId).orElseThrow());
        return detalleRepository.save(detalle);
    }

    @MutationMapping
    public Boolean eliminarDetalleVenta(@Argument Integer id) {
        if (!detalleRepository.existsById(id)) return false;
        detalleRepository.deleteById(id);
        return true;
    }
}
