package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Rol;
import com.sw2parcial.backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RolGraphQLController {

    @Autowired
    private RolRepository rolRepository;

    @QueryMapping
    public List<Rol> listarRoles() {
        List<Rol> roles = rolRepository.findAll();
        return roles != null ? roles : new ArrayList<>();
    }

    @QueryMapping
    public Rol obtenerRol(@Argument Integer id) {
        return rolRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Rol crearRol(@Argument String nombre) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        return rolRepository.save(rol);
    }

    @MutationMapping
    public Rol actualizarRol(@Argument Integer id, @Argument String nombre) {
        Rol rol = rolRepository.findById(id).orElse(null);
        if (rol == null) return null;
        rol.setNombre(nombre);
        return rolRepository.save(rol);
    }

    @MutationMapping
    public Boolean eliminarRol(@Argument Integer id) {
        if (!rolRepository.existsById(id)) return false;
        rolRepository.deleteById(id);
        return true;
    }
}
