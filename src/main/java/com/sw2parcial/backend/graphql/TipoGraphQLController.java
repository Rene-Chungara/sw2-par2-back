package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Tipo;
import com.sw2parcial.backend.repository.TipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class TipoGraphQLController {

    @Autowired
    private TipoRepository tipoRepository;

    @QueryMapping
    public List<Tipo> listarTipos() {
        return tipoRepository.findAll();
    }

    @QueryMapping
    public Tipo obtenerTipo(@Argument Integer id) {
        return tipoRepository.findById(id).orElse(null);
    }

    @MutationMapping
    public Tipo crearTipo(@Argument String nombre, @Argument String descripcion) {
        Tipo tipo = new Tipo();
        tipo.setNombre(nombre);
        tipo.setDescripcion(descripcion);
        return tipoRepository.save(tipo);
    }

    @MutationMapping
    public Tipo actualizarTipo(@Argument Integer id, @Argument String nombre, @Argument String descripcion) {
        Optional<Tipo> optionalTipo = tipoRepository.findById(id);
        if (optionalTipo.isPresent()) {
            Tipo tipo = optionalTipo.get();
            tipo.setNombre(nombre);
            tipo.setDescripcion(descripcion);
            return tipoRepository.save(tipo);
        }
        return null;
    }

    @MutationMapping
    public Boolean eliminarTipo(@Argument Integer id) {
        if (tipoRepository.existsById(id)) {
            tipoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}