package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Rol;
import com.sw2parcial.backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*") // para que Postman o frontend externo acceda
public class RolController {

    @Autowired
    private RolRepository rolRepository;

    @GetMapping
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @PostMapping
    public Rol crear(@RequestBody Rol rol) {
        return rolRepository.save(rol);
    }

    @GetMapping("/{id}")
    public Rol obtenerPorId(@PathVariable Integer id) {
        return rolRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Rol actualizar(@PathVariable Integer id, @RequestBody Rol rolDetalles) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol != null) {
            rol.setNombre(rolDetalles.getNombre());
            return rolRepository.save(rol);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        rolRepository.deleteById(id);
    }
}
