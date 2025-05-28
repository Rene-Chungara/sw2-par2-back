package com.sw2parcial.backend.service;

import com.sw2parcial.backend.dto.LoginRequest;
import com.sw2parcial.backend.dto.LoginResponse;
import com.sw2parcial.backend.dto.RegisterRequest;
import com.sw2parcial.backend.dto.RegisterResponse;
import com.sw2parcial.backend.dto.UsuarioDTO;
import com.sw2parcial.backend.model.Usuario;
import com.sw2parcial.backend.model.Rol;
import com.sw2parcial.backend.repository.UsuarioRepository;
import com.sw2parcial.backend.repository.RolRepository;
import com.sw2parcial.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {
        // Verificar si el correo ya existe
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        // Buscar rol por ID
        Optional<Rol> rolOpt = rolRepository.findById(request.getRolId());
        if (rolOpt.isEmpty()) {
            throw new RuntimeException("Rol no encontrado");
        }

        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setCi(request.getCi());
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setGenero(request.getGenero());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setRol(rolOpt.get());

        // Guardar usuario
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Crear respuesta
        RegisterResponse response = new RegisterResponse();
        response.setSuccess(true);
        response.setMessage("Usuario registrado exitosamente");
        
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuarioGuardado.getId());
        usuarioDTO.setCi(usuarioGuardado.getCi());
        usuarioDTO.setNombre(usuarioGuardado.getNombre());
        usuarioDTO.setTelefono(usuarioGuardado.getTelefono());
        usuarioDTO.setDireccion(usuarioGuardado.getDireccion());
        usuarioDTO.setGenero(usuarioGuardado.getGenero());
        usuarioDTO.setCorreo(usuarioGuardado.getCorreo());
        usuarioDTO.setRolId(usuarioGuardado.getRol().getId());
        usuarioDTO.setRolNombre(usuarioGuardado.getRol().getNombre());
        
        response.setUsuario(usuarioDTO);

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        // Buscar usuario por correo
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(request.getCorreo());
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Credenciales inválidas");
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Generar JWT token
        String token = jwtUtil.generateToken(usuario.getCorreo(), usuario.getRol().getNombre());

        // Crear respuesta
        LoginResponse response = new LoginResponse();
        response.setSuccess(true);
        response.setMessage("Login exitoso");
        response.setToken(token);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setCi(usuario.getCi());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setDireccion(usuario.getDireccion());
        usuarioDTO.setGenero(usuario.getGenero());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setRolId(usuario.getRol().getId());
        usuarioDTO.setRolNombre(usuario.getRol().getNombre());

        response.setUsuario(usuarioDTO);

        return response;
    }
}
