package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.service.VentaService;
import com.sw2parcial.backend.repository.VentaRepository;
import com.sw2parcial.backend.repository.DetalleVentaRepository;
import com.sw2parcial.backend.repository.UsuarioRepository;
import com.sw2parcial.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Controller
public class VentaGraphQLController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // QUERIES - VENTAS
    @QueryMapping
    public List<Venta> listarVentas() {
        return ventaService.obtenerTodasLasVentas();
    }

    @QueryMapping
    public Venta obtenerVenta(@Argument Integer id) {
        return ventaService.obtenerVentaPorId(id).orElse(null);
    }

    @QueryMapping
    public Venta obtenerVentaConDetalles(@Argument Integer id) {
        return ventaService.obtenerVentaConDetalles(id);
    }

    @QueryMapping
    public List<Venta> ventasPorEstado(@Argument String estado) {
        return ventaService.obtenerVentasPorEstado(estado);
    }

    @QueryMapping
    public List<Venta> ventasPorFecha(@Argument String fecha) {
        try {
            LocalDate fechaBusqueda = LocalDate.parse(fecha);
            return ventaService.obtenerVentasPorFecha(fechaBusqueda);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @QueryMapping
    public List<Venta> ventasDelDia() {
        return ventaService.obtenerVentasDelDia();
    }

    @QueryMapping
    public List<Venta> ventasEntreFechas(@Argument String fechaInicio, @Argument String fechaFin) {
        try {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            return ventaService.obtenerVentasEntreFechas(inicio, fin);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // QUERIES - DETALLES VENTA
    @QueryMapping
    public List<DetalleVenta> listarDetallesVenta() {
        return detalleVentaRepository.findAll();
    }

    @QueryMapping
    public DetalleVenta obtenerDetalleVenta(@Argument Integer id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<DetalleVenta> listarDetallesPorVenta(@Argument Integer ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId);
    }

    // MUTATIONS - VENTAS
    @MutationMapping
    public Venta crearVenta(@Argument Integer usuarioId,
            @Argument String fechaVenta,
            @Argument String estado,
            @Argument String canalVenta) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (!usuarioOpt.isPresent()) {
                throw new RuntimeException("Usuario no encontrado");
            }

            Venta venta = new Venta();
            venta.setUsuario(usuarioOpt.get());
            venta.setEstado(estado != null ? estado : "PENDIENTE");
            venta.setCanalVenta(canalVenta);
            venta.setFechaVenta(fechaVenta != null ? LocalDate.parse(fechaVenta) : LocalDate.now());
            venta.setVentaTotal(0.0f); // Se calculará cuando se agreguen detalles

            return ventaRepository.save(venta);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la venta: " + e.getMessage());
        }
    }

    @MutationMapping
    public Venta crearVentaCompleta(@Argument Integer usuarioId,
            @Argument String fechaVenta,
            @Argument String estado,
            @Argument String canalVenta,
            @Argument List<DetalleVentaInput> detalles) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (!usuarioOpt.isPresent()) {
                throw new RuntimeException("Usuario no encontrado");
            }

            // Crear venta
            Venta venta = new Venta();
            venta.setUsuario(usuarioOpt.get());
            venta.setEstado(estado != null ? estado : "PENDIENTE");
            venta.setCanalVenta(canalVenta);
            venta.setFechaVenta(fechaVenta != null ? LocalDate.parse(fechaVenta) : LocalDate.now());

            // Crear detalles
            List<DetalleVenta> listaDetalles = new ArrayList<>();
            for (DetalleVentaInput detalleInput : detalles) {
                Optional<Producto> productoOpt = productoRepository.findById(detalleInput.getProductoId());
                if (!productoOpt.isPresent()) {
                    throw new RuntimeException("Producto con ID " + detalleInput.getProductoId() + " no encontrado");
                }

                Producto producto = productoOpt.get();
                if (producto.getStock() < detalleInput.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
                }

                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(producto);
                detalle.setCantidad(detalleInput.getCantidad());
                detalle.setPrecioUnitario(detalleInput.getPrecioUnitario());
                listaDetalles.add(detalle);
            }

            return ventaService.crearVenta(venta, listaDetalles);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la venta: " + e.getMessage());
        }
    }

    @MutationMapping
    public Venta actualizarVenta(@Argument Integer id,
            @Argument Integer usuarioId,
            @Argument String fechaVenta,
            @Argument Float ventaTotal,
            @Argument String estado,
            @Argument String canalVenta) {
        try {
            Optional<Venta> ventaOpt = ventaRepository.findById(id);
            if (!ventaOpt.isPresent()) {
                return null;
            }

            Venta venta = ventaOpt.get();

            if (usuarioId != null) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
                if (usuarioOpt.isPresent()) {
                    venta.setUsuario(usuarioOpt.get());
                }
            }

            if (fechaVenta != null) {
                venta.setFechaVenta(LocalDate.parse(fechaVenta));
            }
            if (ventaTotal != null) {
                venta.setVentaTotal(ventaTotal);
            }
            if (estado != null) {
                venta.setEstado(estado);
            }
            if (canalVenta != null) {
                venta.setCanalVenta(canalVenta);
            }

            return ventaRepository.save(venta);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la venta: " + e.getMessage());
        }
    }

    @MutationMapping
    public Venta cambiarEstadoVenta(@Argument Integer id, @Argument String nuevoEstado) {
        return ventaService.cambiarEstadoVenta(id, nuevoEstado);
    }

    @MutationMapping
    public Boolean eliminarVenta(@Argument Integer id) {
        return ventaService.eliminarVenta(id);
    }

    // MUTATIONS - DETALLES VENTA
    @MutationMapping
    public DetalleVenta crearDetalleVenta(@Argument Integer productoId,
            @Argument Integer cantidad,
            @Argument Float precioUnitario,
            @Argument Integer ventaId) {
        try {
            Optional<Producto> productoOpt = productoRepository.findById(productoId);
            Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);

            if (!productoOpt.isPresent()) {
                throw new RuntimeException("Producto no encontrado");
            }
            if (!ventaOpt.isPresent()) {
                throw new RuntimeException("Venta no encontrada");
            }

            Producto producto = productoOpt.get();
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setVenta(ventaOpt.get());

            DetalleVenta detalleGuardado = detalleVentaRepository.save(detalle);

            // Actualizar stock del producto
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            // Recalcular total de la venta
            Venta venta = ventaOpt.get();
            List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);
            Float nuevoTotal = detalles.stream()
                    .map(d -> d.getCantidad() * d.getPrecioUnitario())
                    .reduce(0.0f, Float::sum);
            venta.setVentaTotal(nuevoTotal);
            ventaRepository.save(venta);

            return detalleGuardado;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el detalle de venta: " + e.getMessage());
        }
    }

    @MutationMapping
    public DetalleVenta actualizarDetalleVenta(@Argument Integer id,
            @Argument Integer productoId,
            @Argument Integer cantidad,
            @Argument Float precioUnitario,
            @Argument Integer ventaId) {
        try {
            Optional<DetalleVenta> detalleOpt = detalleVentaRepository.findById(id);
            if (!detalleOpt.isPresent()) {
                return null;
            }

            DetalleVenta detalle = detalleOpt.get();
            Integer cantidadAnterior = detalle.getCantidad();

            if (productoId != null) {
                Optional<Producto> productoOpt = productoRepository.findById(productoId);
                if (productoOpt.isPresent()) {
                    detalle.setProducto(productoOpt.get());
                }
            }
            if (cantidad != null) {
                detalle.setCantidad(cantidad);
            }
            if (precioUnitario != null) {
                detalle.setPrecioUnitario(precioUnitario);
            }
            if (ventaId != null) {
                Optional<Venta> ventaOpt = ventaRepository.findById(ventaId);
                if (ventaOpt.isPresent()) {
                    detalle.setVenta(ventaOpt.get());
                }
            }

            DetalleVenta detalleActualizado = detalleVentaRepository.save(detalle);

            // Actualizar stock si cambió la cantidad
            if (cantidad != null && !cantidad.equals(cantidadAnterior)) {
                Producto producto = detalle.getProducto();
                Integer diferencia = cantidadAnterior - cantidad;
                producto.setStock(producto.getStock() + diferencia);
                productoRepository.save(producto);
            }

            return detalleActualizado;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el detalle de venta: " + e.getMessage());
        }
    }

    @MutationMapping
    public Boolean eliminarDetalleVenta(@Argument Integer id) {
        try {
            Optional<DetalleVenta> detalleOpt = detalleVentaRepository.findById(id);
            if (!detalleOpt.isPresent()) {
                return false;
            }

            DetalleVenta detalle = detalleOpt.get();

            // Restaurar stock
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);

            detalleVentaRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el detalle de venta: " + e.getMessage());
        }
    }

    // Clase interna para el input de detalle de venta
    public static class DetalleVentaInput {
        private Integer productoId;
        private Integer cantidad;
        private Float precioUnitario;

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

    @QueryMapping
    public List<Venta> ventasPorTelefono(@Argument String telefono) {
        return ventaRepository.findByUsuarioTelefono(telefono);
    }

}