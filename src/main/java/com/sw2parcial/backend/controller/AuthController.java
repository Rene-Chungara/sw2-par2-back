package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.dto.LoginRequest;
import com.sw2parcial.backend.dto.LoginResponse;
import com.sw2parcial.backend.dto.RegisterRequest;
import com.sw2parcial.backend.dto.RegisterResponse;
//import com.sw2parcial.backend.model.Usuario;
//import com.sw2parcial.backend.model.Rol;
//import com.sw2parcial.backend.repository.UsuarioRepository;
//import com.sw2parcial.backend.repository.RolRepository;
import com.sw2parcial.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            RegisterResponse errorResponse = new RegisterResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            LoginResponse errorResponse = new LoginResponse();
            errorResponse.setSuccess(false);
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}