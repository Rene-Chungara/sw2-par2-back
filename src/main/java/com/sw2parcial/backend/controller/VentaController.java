package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.service.VentaService;
import com.sw2parcial.backend.repository.DetalleVentaRepository;
import com.sw2parcial.backend.repository.UsuarioRepository;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.dto.VentaRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    // Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Integer id) {
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener venta con detalles
    @GetMapping("/{id}/detalles")
    public ResponseEntity<Venta> obtenerVentaConDetalles(@PathVariable Integer id) {
        Venta venta = ventaService.obtenerVentaConDetalles(id);
        if (venta != null) {
            return ResponseEntity.ok(venta);
        }
        return ResponseEntity.notFound().build();
    }

    // Crear nueva venta usando DTO
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearVenta(@RequestBody VentaRequestDTO ventaRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Buscar usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(ventaRequest.getUsuarioId());
            if (!usuarioOpt.isPresent()) {
                response.put("success", false);
                response.put("message", "Usuario no encontrado");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Crear venta
            Venta venta = new Venta();
            venta.setUsuario(usuarioOpt.get());
            venta.setEstado(ventaRequest.getEstado() != null ? ventaRequest.getEstado() : "PENDIENTE");
            venta.setCanalVenta(ventaRequest.getCanalVenta());
            venta.setFechaVenta(ventaRequest.getFechaVenta() != null ? ventaRequest.getFechaVenta() : LocalDate.now());

            // Crear detalles
            List<DetalleVenta> detalles = new ArrayList<>();
            for (VentaRequestDTO.DetalleVentaDTO detalleDTO : ventaRequest.getDetalles()) {
                Optional<Producto> productoOpt = productoRepository.findById(detalleDTO.getProductoId());
                if (!productoOpt.isPresent()) {
                    response.put("success", false);
                    response.put("message", "Producto con ID " + detalleDTO.getProductoId() + " no encontrado");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                // Verificar stock
                Producto producto = productoOpt.get();
                if (producto.getStock() < detalleDTO.getCantidad()) {
                    response.put("success", false);
                    response.put("message", "Stock insuficiente para el producto: " + producto.getNombre());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                DetalleVenta detalle = new DetalleVenta();
                detalle.setProducto(producto);
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalles.add(detalle);
            }

            Venta ventaCreada = ventaService.crearVenta(venta, detalles);

            response.put("success", true);
            response.put("message", "Venta creada exitosamente");
            response.put("venta", ventaCreada);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al crear la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Actualizar venta
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        Map<String, Object> response = new HashMap<>();

        try {
            Venta ventaActualizada = ventaService.actualizarVenta(id, venta);
            if (ventaActualizada != null) {
                response.put("success", true);
                response.put("message", "Venta actualizada exitosamente");
                response.put("venta", ventaActualizada);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Venta no encontrada");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar venta
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarVenta(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            boolean eliminada = ventaService.eliminarVenta(id);
            if (eliminada) {
                response.put("success", true);
                response.put("message", "Venta eliminada exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Venta no encontrada");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar la venta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Buscar ventas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Venta>> obtenerVentasPorEstado(@PathVariable String estado) {
        List<Venta> ventas = ventaService.obtenerVentasPorEstado(estado);
        return ResponseEntity.ok(ventas);
    }

    // Buscar ventas por fecha
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Venta>> obtenerVentasPorFecha(@PathVariable String fecha) {
        try {
            LocalDate fechaBusqueda = LocalDate.parse(fecha);
            List<Venta> ventas = ventaService.obtenerVentasPorFecha(fechaBusqueda);
            return ResponseEntity.ok(ventas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener ventas del día
    @GetMapping("/hoy")
    public ResponseEntity<List<Venta>> obtenerVentasDelDia() {
        List<Venta> ventas = ventaService.obtenerVentasDelDia();
        return ResponseEntity.ok(ventas);
    }

    // Cambiar estado de venta
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> cambiarEstadoVenta(@PathVariable Integer id,
            @RequestBody Map<String, String> estadoData) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nuevoEstado = estadoData.get("estado");
            Venta ventaActualizada = ventaService.cambiarEstadoVenta(id, nuevoEstado);

            if (ventaActualizada != null) {
                response.put("success", true);
                response.put("message", "Estado actualizado exitosamente");
                response.put("venta", ventaActualizada);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Venta no encontrada");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cambiar el estado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Obtener detalles de una venta específica
    @GetMapping("/{id}/detalle")
    public ResponseEntity<List<DetalleVenta>> obtenerDetallesPorVenta(@PathVariable Integer id) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(id);
        return ResponseEntity.ok(detalles);
    }
}