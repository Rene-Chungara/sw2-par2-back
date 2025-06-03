package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Proveedor;
import com.sw2parcial.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProveedorGraphQLController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @QueryMapping
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @QueryMapping
    public Proveedor obtenerProveedor(@Argument Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Proveedor> buscarProveedorPorNombre(@Argument String nombre) {
        return proveedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @MutationMapping
    public Proveedor crearProveedor(@Argument String nombre, @Argument String origen) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(nombre);
        proveedor.setOrigen(origen);
        return proveedorRepository.save(proveedor);
    }

    @MutationMapping
    public Proveedor actualizarProveedor(@Argument Integer id, @Argument String nombre, @Argument String origen) {
        Proveedor proveedor = proveedorRepository.findById(id).orElse(null);
        if (proveedor == null) return null;
        proveedor.setNombre(nombre);
        proveedor.setOrigen(origen);
        return proveedorRepository.save(proveedor);
    }

    @MutationMapping
    public Boolean eliminarProveedor(@Argument Integer id) {
        if (!proveedorRepository.existsById(id)) return false;
        proveedorRepository.deleteById(id);
        return true;
    }

    @QueryMapping
    public List<Proveedor> buscarProveedorPorOrigen(@Argument String origen) {
        return proveedorRepository.findByOrigenContainingIgnoreCase(origen);
    }
}
