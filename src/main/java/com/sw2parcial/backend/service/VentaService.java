package com.sw2parcial.backend.service;

import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.ReporteVentaPorTipo;
import com.sw2parcial.backend.repository.VentaRepository;
import com.sw2parcial.backend.repository.DetalleVentaRepository;
import com.sw2parcial.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todas las ventas
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    // Obtener venta por ID
    public Optional<Venta> obtenerVentaPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    // Obtener venta con detalles
    public Venta obtenerVentaConDetalles(Integer id) {
        return ventaRepository.findByIdWithDetalles(id);
    }

    // Crear nueva venta
    @Transactional
    public Venta crearVenta(Venta venta, List<DetalleVenta> detalles) {
        // Establecer fecha actual si no se proporciona
        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDate.now());
        }

        // Establecer estado por defecto
        if (venta.getEstado() == null) {
            venta.setEstado("PENDIENTE");
        }

        // Calcular total de la venta
        Float total = 0.0f;
        for (DetalleVenta detalle : detalles) {
            total += detalle.getCantidad() * detalle.getPrecioUnitario();
        }
        venta.setVentaTotal(total);

        // Guardar la venta
        Venta ventaGuardada = ventaRepository.save(venta);

        // Guardar los detalles y actualizar stock
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaRepository.save(detalle);

            // Actualizar stock del producto
            actualizarStockProducto(detalle.getProducto().getId(), detalle.getCantidad());
        }

        return ventaGuardada;
    }

    // Actualizar venta
    public Venta actualizarVenta(Integer id, Venta ventaActualizada) {
        Optional<Venta> ventaExistente = ventaRepository.findById(id);
        if (ventaExistente.isPresent()) {
            Venta venta = ventaExistente.get();
            venta.setVentaTotal(ventaActualizada.getVentaTotal());
            venta.setFechaVenta(ventaActualizada.getFechaVenta());
            venta.setEstado(ventaActualizada.getEstado());
            venta.setCanalVenta(ventaActualizada.getCanalVenta());
            return ventaRepository.save(venta);
        }
        return null;
    }

    // Eliminar venta
    @Transactional
    public boolean eliminarVenta(Integer id) {
        if (ventaRepository.existsById(id)) {
            // Primero eliminar los detalles
            List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(id);
            for (DetalleVenta detalle : detalles) {
                // Restaurar stock
                restaurarStockProducto(detalle.getProducto().getId(), detalle.getCantidad());
            }
            detalleVentaRepository.deleteAll(detalles);
            
            // Luego eliminar la venta
            ventaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Buscar ventas por usuario
    public List<Venta> obtenerVentasPorUsuario(Usuario usuario) {
        return ventaRepository.findByUsuario(usuario);
    }

    // Buscar ventas por estado
    public List<Venta> obtenerVentasPorEstado(String estado) {
        return ventaRepository.findByEstado(estado);
    }

    // Buscar ventas por fecha
    public List<Venta> obtenerVentasPorFecha(LocalDate fecha) {
        return ventaRepository.findByFechaVenta(fecha);
    }

    // Buscar ventas entre fechas
    public List<Venta> obtenerVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }

    // Obtener ventas del día
    public List<Venta> obtenerVentasDelDia() {
        return ventaRepository.findVentasDelDia();
    }

    // Cambiar estado de venta
    public Venta cambiarEstadoVenta(Integer id, String nuevoEstado) {
        Optional<Venta> ventaOptional = ventaRepository.findById(id);
        if (ventaOptional.isPresent()) {
            Venta venta = ventaOptional.get();
            venta.setEstado(nuevoEstado);
            return ventaRepository.save(venta);
        }
        return null;
    }

    // Métodos privados para manejo de stock
    private void actualizarStockProducto(Integer productoId, Integer cantidadVendida) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setStock(producto.getStock() - cantidadVendida);
            productoRepository.save(producto);
        }
    }

    private void restaurarStockProducto(Integer productoId, Integer cantidadARestaurar) {
        Optional<Producto> productoOptional = productoRepository.findById(productoId);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setStock(producto.getStock() + cantidadARestaurar);
            productoRepository.save(producto);
        }
    }

    public List<ReporteVentaPorTipo> reporteVentasPorTipoProducto() {
        return ventaRepository.obtenerReportePorTipoProducto();
    }
}