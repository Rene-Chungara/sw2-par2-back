package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.NotaEntrada;
import com.sw2parcial.backend.repository.NotaEntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nota-entrada")
@CrossOrigin(origins = "*")
public class NotaEntradaController {

    @Autowired
    private NotaEntradaRepository notaEntradaRepository;

    @GetMapping
    public List<NotaEntrada> listarTodos() {
        return notaEntradaRepository.findAll();
    }

    @PostMapping
    public NotaEntrada crear(@RequestBody NotaEntrada notaEntrada) {
        return notaEntradaRepository.save(notaEntrada);
    }

    @GetMapping("/{id}")
    public NotaEntrada obtenerPorId(@PathVariable Integer id) {
        return notaEntradaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public NotaEntrada actualizar(@PathVariable Integer id, @RequestBody NotaEntrada datos) {
        NotaEntrada nota = notaEntradaRepository.findById(id).orElse(null);
        if (nota != null) {
            nota.setFecha(datos.getFecha());
            nota.setCostoTotal(datos.getCostoTotal());
            nota.setLote(datos.getLote());
            nota.setProveedor(datos.getProveedor());
            return notaEntradaRepository.save(nota);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        notaEntradaRepository.deleteById(id);
    }

    // Filtro por nombre del proveedor
    @GetMapping("/buscar/proveedor/{nombre}")
    public List<NotaEntrada> buscarPorProveedor(@PathVariable String nombre) {
        return notaEntradaRepository.findByProveedorNombreContainingIgnoreCase(nombre);
    }
}
