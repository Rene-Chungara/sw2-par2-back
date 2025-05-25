package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Integer id, @RequestBody Usuario usuarioDetalles) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNombre(usuarioDetalles.getNombre());
            usuario.setCi(usuarioDetalles.getCi());
            usuario.setCorreo(usuarioDetalles.getCorreo());
            usuario.setTelefono(usuarioDetalles.getTelefono());
            usuario.setGenero(usuarioDetalles.getGenero());
            usuario.setDireccion(usuarioDetalles.getDireccion());
            usuario.setContrasena(usuarioDetalles.getContrasena());
            usuario.setRol(usuarioDetalles.getRol());
            // Solo actualizar la contraseña si se proporciona
            if (usuarioDetalles.getContrasena() != null && !usuarioDetalles.getContrasena().isEmpty()) {
                usuario.setContrasena(passwordEncoder.encode(usuarioDetalles.getContrasena()));
            }
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
    }
}
