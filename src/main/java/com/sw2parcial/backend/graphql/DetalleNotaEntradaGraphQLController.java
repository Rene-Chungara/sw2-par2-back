package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.DetalleNotaEntrada;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.NotaEntrada;
import com.sw2parcial.backend.repository.DetalleNotaEntradaRepository;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.repository.NotaEntradaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class DetalleNotaEntradaGraphQLController {

    @Autowired
    private DetalleNotaEntradaRepository detalleRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private NotaEntradaRepository notaEntradaRepository;

    @QueryMapping
    public List<DetalleNotaEntrada> listarDetallesNotaEntrada() {
        return detalleRepository.findAll();
    }

    @QueryMapping
    public DetalleNotaEntrada obtenerDetalleNotaEntrada(@Argument Integer id) {
        return detalleRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<DetalleNotaEntrada> listarDetallesPorNota(@Argument Integer notaEntradaId) {
        return detalleRepository.findByNotaEntradaId(notaEntradaId);
    }

    @MutationMapping
    public DetalleNotaEntrada crearDetalleNotaEntrada(@Argument Integer productoId, @Argument Integer cantidad, @Argument Float costoUnitario, @Argument Integer notaEntradaId) {
        Producto producto = productoRepository.findById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        NotaEntrada nota = notaEntradaRepository.findById(notaEntradaId)
        .orElseThrow(() -> new RuntimeException("Nota de entrada no encontrada con ID: " + notaEntradaId));

        DetalleNotaEntrada detalle = new DetalleNotaEntrada();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setCostoUnitario(costoUnitario);
        detalle.setNotaEntrada(nota);
        return detalleRepository.save(detalle);
    }

    @MutationMapping
    public DetalleNotaEntrada actualizarDetalleNotaEntrada(@Argument Integer id, @Argument Integer productoId, @Argument Integer cantidad, @Argument Float costoUnitario, @Argument Integer notaEntradaId) {
        DetalleNotaEntrada detalle = detalleRepository.findById(id).orElse(null);
        if (detalle == null) return null;
        Producto producto = productoRepository.findById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));

        NotaEntrada nota = notaEntradaRepository.findById(notaEntradaId)
        .orElseThrow(() -> new RuntimeException("Nota de entrada no encontrada con ID: " + notaEntradaId));
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setCostoUnitario(costoUnitario);
        detalle.setNotaEntrada(nota);
        return detalleRepository.save(detalle);
    }

    @MutationMapping
    public Boolean eliminarDetalleNotaEntrada(@Argument Integer id) {
        if (!detalleRepository.existsById(id)) return false;
        detalleRepository.deleteById(id);
        return true;
    }
}
