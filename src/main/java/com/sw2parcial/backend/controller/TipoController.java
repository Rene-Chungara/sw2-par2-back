package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Tipo;
import com.sw2parcial.backend.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos")
@CrossOrigin(origins = "*")
public class TipoController {

    @Autowired
    private TipoRepository tipoRepository;

    @GetMapping
    public List<Tipo> listarTodos() {
        return tipoRepository.findAll();
    }

    @PostMapping
    public Tipo crear(@RequestBody Tipo tipo) {
        return tipoRepository.save(tipo);
    }

    @GetMapping("/{id}")
    public Tipo obtenerPorId(@PathVariable Integer id) {
        return tipoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Tipo actualizar(@PathVariable Integer id, @RequestBody Tipo tipoDetalles) {
        Tipo tipo = tipoRepository.findById(id).orElse(null);
        if (tipo != null) {
            tipo.setNombre(tipoDetalles.getNombre());
            tipo.setDescripcion(tipoDetalles.getDescripcion());
            return tipoRepository.save(tipo);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        tipoRepository.deleteById(id);
    }
}
