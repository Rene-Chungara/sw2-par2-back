package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.Tipo;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.repository.TipoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
    /* 
    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }*/

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        Integer tipoId = producto.getTipo().getId();
        Tipo tipoReal = tipoRepository.findById(tipoId)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + tipoId));
        producto.setTipo(tipoReal);

        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @RequestBody Producto detalles) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            producto.setNombre(detalles.getNombre());
            producto.setPrecioVenta(detalles.getPrecioVenta());
            producto.setImagen(detalles.getImagen());
            producto.setStock(detalles.getStock());
            producto.setDescripcion(detalles.getDescripcion());
            producto.setTipo(detalles.getTipo());
            return productoRepository.save(producto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoRepository.deleteById(id);
    }

    @GetMapping("/tipo/{nombre}")
    public List<Producto> obtenerPorTipo(@PathVariable String nombre) {
        return productoRepository.findByTipoNombre(nombre);
    }

}
