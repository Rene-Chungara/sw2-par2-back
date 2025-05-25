package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Proveedor;
import com.sw2parcial.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @PostMapping
    public Proveedor crear(@RequestBody Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @GetMapping("/{id}")
    public Proveedor obtenerPorId(@PathVariable Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable Integer id, @RequestBody Proveedor datos) {
        Proveedor proveedor = proveedorRepository.findById(id).orElse(null);
        if (proveedor != null) {
            proveedor.setNombre(datos.getNombre());
            proveedor.setOrigen(datos.getOrigen());
            return proveedorRepository.save(proveedor);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        proveedorRepository.deleteById(id);
    }

    // Filtros
    @GetMapping("/buscar/nombre/{nombre}")
    public List<Proveedor> buscarPorNombre(@PathVariable String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @GetMapping("/buscar/origen/{origen}")
    public List<Proveedor> buscarPorOrigen(@PathVariable String origen) {
        return proveedorRepository.findByOrigenContainingIgnoreCase(origen);
    }
}
