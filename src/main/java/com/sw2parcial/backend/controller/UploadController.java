package com.sw2parcial.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@RestController
@RequestMapping("/uploads")
@CrossOrigin(origins = "*") // para permitir subida desde Angular
public class UploadController {

    private static final String UPLOAD_DIR = "uploads";

    @PostMapping
    public ResponseEntity<?> subirArchivo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo vacío");
            }

            // Crear carpeta si no existe
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Limpiar nombre y definir destino
            String nombreArchivo = StringUtils.cleanPath(file.getOriginalFilename());
            Path destino = Paths.get(UPLOAD_DIR).resolve(nombreArchivo);
            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            // Retornar ruta como JSON
            String ruta = "/uploads/" + nombreArchivo;
            return ResponseEntity.ok().body("{\"path\": \"" + ruta + "\"}");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al subir imagen");
        }
    }
}
