package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.dto.VentaRequestDTO;
import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import com.sw2parcial.backend.repository.ProductoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VentaGraphQLController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @QueryMapping
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }

    @QueryMapping
    public Venta obtenerVenta(@Argument Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Venta crearVenta(@Argument VentaRequestDTO ventaDTO) {
        try {
            Venta venta = new Venta();
            venta.setVentaTotal(ventaDTO.getVentaTotal());
            venta.setFechaVenta(ventaDTO.getFechaVenta()); // ya es LocalDate
            venta.setCanalVenta(ventaDTO.getCanalVenta());
            venta.setEstado(ventaDTO.getEstado());

            List<DetalleVenta> detalles = new ArrayList<>();
            for (VentaRequestDTO.DetalleVentaDTO d : ventaDTO.getDetalles()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(productoRepository.findById(d.getProductoId()).orElseThrow());
                detalle.setCantidad(d.getCantidad());
                detalle.setPrecioUnitario(d.getPrecioUnitario());
                detalle.setVenta(venta); // asigna la venta a cada detalle
                detalles.add(detalle);
            }

            venta.setDetalleVentas(detalles);
            return ventaRepository.save(venta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    @MutationMapping
    public Venta actualizarVenta(
            @Argument Integer id,
            @Argument String fechaVenta,
            @Argument Float ventaTotal,
            @Argument String estado,
            @Argument String canalVenta
    ) {
        Venta venta = ventaRepository.findById(id).orElse(null);
        if (venta == null) return null;
        venta.setFechaVenta(LocalDate.parse(fechaVenta));
        venta.setVentaTotal(ventaTotal);
        venta.setEstado(estado);
        venta.setCanalVenta(canalVenta);
        return ventaRepository.save(venta);
    }

    @MutationMapping
    public Boolean eliminarVenta(@Argument Integer id) {
        if (!ventaRepository.existsById(id)) return false;
        ventaRepository.deleteById(id);
        return true;
    }
}
