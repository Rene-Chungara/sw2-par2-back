package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Rol;
import com.sw2parcial.backend.model.Permiso;
import com.sw2parcial.backend.model.RolPermiso;
import com.sw2parcial.backend.repository.RolRepository;
import com.sw2parcial.backend.repository.PermisoRepository;
import com.sw2parcial.backend.repository.RolPermisoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RolPermisoGraphQLController {

    @Autowired
    private RolPermisoRepository rolPermisoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @QueryMapping
    public List<RolPermiso> listarRolPermisos() {
        return rolPermisoRepository.findAll();
    }

    @QueryMapping
    public RolPermiso obtenerRolPermiso(@Argument Integer id) {
        return rolPermisoRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public RolPermiso crearRolPermiso(@Argument Integer rolId, @Argument Integer permisoId) {
        Rol rol = rolRepository.findById(rolId).orElseThrow();
        Permiso permiso = permisoRepository.findById(permisoId).orElseThrow();

        RolPermiso rp = new RolPermiso();
        rp.setRol(rol);
        rp.setPermiso(permiso);
        return rolPermisoRepository.save(rp);
    }

    @MutationMapping
    public Boolean eliminarRolPermiso(@Argument Integer id) {
        if (!rolPermisoRepository.existsById(id)) return false;
        rolPermisoRepository.deleteById(id);
        return true;
    }
}
