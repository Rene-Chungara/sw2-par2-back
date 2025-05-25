package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.RolPermiso;
import com.sw2parcial.backend.repository.RolPermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol-permisos")
@CrossOrigin(origins = "*")
public class RolPermisoController {

    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @GetMapping
    public List<RolPermiso> listarTodos() {
        return rolPermisoRepository.findAll();
    }

    @PostMapping
    public RolPermiso crear(@RequestBody RolPermiso rolPermiso) {
        return rolPermisoRepository.save(rolPermiso);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        rolPermisoRepository.deleteById(id);
    }
}
