package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Permiso;
import com.sw2parcial.backend.repository.PermisoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PermisoGraphQLController {

    @Autowired
    private PermisoRepository permisoRepository;

    @QueryMapping
    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }

    @QueryMapping
    public Permiso obtenerPermiso(@Argument Integer id) {
        return permisoRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Permiso crearPermiso(@Argument String nombre) {
        Permiso permiso = new Permiso();
        permiso.setNombre(nombre);
        return permisoRepository.save(permiso);
    }

    @MutationMapping
    public Permiso actualizarPermiso(@Argument Integer id, @Argument String nombre) {
        Permiso permiso = permisoRepository.findById(id).orElse(null);
        if (permiso == null) return null;
        permiso.setNombre(nombre);
        return permisoRepository.save(permiso);
    }

    @MutationMapping
    public Boolean eliminarPermiso(@Argument Integer id) {
        if (!permisoRepository.existsById(id)) return false;
        permisoRepository.deleteById(id);
        return true;
    }
}
