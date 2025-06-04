package com.sw2parcial.backend.graphql;

import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.model.Rol;
import com.sw2parcial.backend.repository.UsuarioRepository;
import com.sw2parcial.backend.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UsuarioGraphQLController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;

    @QueryMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario obtenerUsuario(@Argument Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public Usuario buscarUsuarioPorCorreo(@Argument String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @MutationMapping
    public Usuario crearUsuario(@Argument String ci, @Argument String nombre, @Argument String telefono,
                                @Argument String direccion, @Argument String genero, @Argument String correo,
                                @Argument String contrasena, @Argument int rolId) {
        try {
            System.out.println("Iniciando creación de usuario");

            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));
            System.out.println("Rol encontrado: " + rol.getNombre());

            Usuario usuario = new Usuario();
            usuario.setCi(ci);
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setGenero(genero);
            usuario.setCorreo(correo);
            usuario.setContrasena(passwordEncoder.encode(contrasena));
            usuario.setRol(rol);

            Usuario creado = usuarioRepository.save(usuario);
            System.out.println("Usuario guardado con ID: " + creado.getId());
            return creado;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @MutationMapping
    public Usuario actualizarUsuario(@Argument int id,
                                    @Argument String ci,
                                    @Argument String nombre,
                                    @Argument String telefono,
                                    @Argument String direccion,
                                    @Argument String genero,
                                    @Argument String correo,
                                    @Argument String contrasena,
                                    @Argument int rolId) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

            usuario.setCi(ci);
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setDireccion(direccion);
            usuario.setGenero(genero);
            usuario.setCorreo(correo);

            if (contrasena != null && !contrasena.isBlank()) {
                usuario.setContrasena(passwordEncoder.encode(contrasena));
            }

            Rol rol = rolRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + rolId));
            usuario.setRol(rol);

            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @MutationMapping
    public Boolean eliminarUsuario(@Argument Integer id) {
        if (!usuarioRepository.existsById(id)) return false;
        usuarioRepository.deleteById(id);
        return true;
    }
}
