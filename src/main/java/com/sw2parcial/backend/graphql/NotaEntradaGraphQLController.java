package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.NotaEntrada;
import com.sw2parcial.backend.model.Proveedor;
import com.sw2parcial.backend.repository.NotaEntradaRepository;
import com.sw2parcial.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class NotaEntradaGraphQLController {

    @Autowired
    private NotaEntradaRepository notaEntradaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @QueryMapping
    public List<NotaEntrada> listarNotasEntrada() {
        return notaEntradaRepository.findAll();
    }

    @QueryMapping
    public NotaEntrada obtenerNotaEntrada(@Argument Integer id) {
        return notaEntradaRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<NotaEntrada> buscarNotaEntradaPorProveedor(@Argument String nombre) {
        return notaEntradaRepository.findByProveedorNombreContainingIgnoreCase(nombre);
    }

    @MutationMapping
    public NotaEntrada crearNotaEntrada(@Argument String fecha, @Argument String lote, @Argument Float costoTotal, @Argument Integer proveedorId) {
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        NotaEntrada nota = new NotaEntrada();
        nota.setFecha(LocalDate.parse(fecha));
        nota.setLote(lote);
        nota.setCostoTotal(costoTotal);
        nota.setProveedor(proveedor);
        return notaEntradaRepository.save(nota);
    }

    @MutationMapping
    public NotaEntrada actualizarNotaEntrada(@Argument Integer id, @Argument String fecha, @Argument String lote, @Argument Float costoTotal, @Argument Integer proveedorId) {
        NotaEntrada nota = notaEntradaRepository.findById(id).orElse(null);
        if (nota == null) return null;
        Proveedor proveedor = proveedorRepository.findById(proveedorId).orElseThrow();
        nota.setFecha(LocalDate.parse(fecha));
        nota.setLote(lote);
        nota.setCostoTotal(costoTotal);
        nota.setProveedor(proveedor);
        return notaEntradaRepository.save(nota);
    }

    @MutationMapping
    public Boolean eliminarNotaEntrada(@Argument Integer id) {
        if (!notaEntradaRepository.existsById(id)) return false;
        notaEntradaRepository.deleteById(id);
        return true;
    }
}
