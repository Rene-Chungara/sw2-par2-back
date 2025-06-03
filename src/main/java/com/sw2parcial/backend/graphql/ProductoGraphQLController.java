package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.Tipo;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductoGraphQLController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @QueryMapping
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @QueryMapping
    public Producto obtenerProducto(@Argument Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Producto> productosPorTipo(@Argument String nombre) {
        return productoRepository.findByTipoNombre(nombre);
    }

    @MutationMapping
    public Producto crearProducto(
            @Argument String nombre,
            @Argument Float precioVenta,
            @Argument String imagen,
            @Argument Integer stock,
            @Argument String descripcion,
            @Argument Integer tipoId
    ) {
        Tipo tipo = tipoRepository.findById(tipoId)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + tipoId));
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecioVenta(precioVenta);
        producto.setImagen(imagen);
        producto.setStock(stock);
        producto.setDescripcion(descripcion);
        producto.setTipo(tipo);
        return productoRepository.save(producto);
    }

    @MutationMapping
    public Producto actualizarProducto(
            @Argument Integer id,
            @Argument String nombre,
            @Argument Float precioVenta,
            @Argument String imagen,
            @Argument Integer stock,
            @Argument String descripcion,
            @Argument Integer tipoId
    ) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) return null;
        Tipo tipo = tipoRepository.findById(tipoId)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + tipoId));

        producto.setNombre(nombre);
        producto.setPrecioVenta(precioVenta);
        producto.setImagen(imagen);
        producto.setStock(stock);
        producto.setDescripcion(descripcion);
        producto.setTipo(tipo);
        return productoRepository.save(producto);
    }

    @MutationMapping
    public Boolean eliminarProducto(@Argument Integer id) {
        if (!productoRepository.existsById(id)) return false;
        productoRepository.deleteById(id);
        return true;
    }
}
