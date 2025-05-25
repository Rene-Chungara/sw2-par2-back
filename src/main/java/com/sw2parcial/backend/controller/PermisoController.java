package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Permiso;
import com.sw2parcial.backend.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permisos")
@CrossOrigin(origins = "*")
public class PermisoController {

    @Autowired
    private PermisoRepository permisoRepository;

    @GetMapping
    public List<Permiso> listarTodos() {
        return permisoRepository.findAll();
    }

    @PostMapping
    public Permiso crear(@RequestBody Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @GetMapping("/{id}")
    public Permiso obtenerPorId(@PathVariable Integer id) {
        return permisoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Permiso actualizar(@PathVariable Integer id, @RequestBody Permiso permisoDetalles) {
        Permiso permiso = permisoRepository.findById(id).orElse(null);
        if (permiso != null) {
            permiso.setNombre(permisoDetalles.getNombre());
            return permisoRepository.save(permiso);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        permisoRepository.deleteById(id);
    }
}
