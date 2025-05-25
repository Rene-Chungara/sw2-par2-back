package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Precio;
import com.sw2parcial.backend.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/precios")
@CrossOrigin(origins = "*")
public class PrecioController {

    @Autowired
    private PrecioRepository precioRepository;

    @GetMapping
    public List<Precio> listarTodos() {
        return precioRepository.findAll();
    }

    @PostMapping
    public Precio crear(@RequestBody Precio precio) {
        return precioRepository.save(precio);
    }

    @GetMapping("/{id}")
    public Precio obtenerPorId(@PathVariable Integer id) {
        return precioRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Precio actualizar(@PathVariable Integer id, @RequestBody Precio detalles) {
        Precio precio = precioRepository.findById(id).orElse(null);
        if (precio != null) {
            precio.setFecha(detalles.getFecha());
            precio.setPrecio(detalles.getPrecio());
            precio.setProducto(detalles.getProducto());
            return precioRepository.save(precio);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        precioRepository.deleteById(id);
    }

    @GetMapping("/producto/{productoId}")
    public List<Precio> obtenerPorProducto(@PathVariable Integer productoId) {
        return precioRepository.findByProductoId(productoId);
    }
}
