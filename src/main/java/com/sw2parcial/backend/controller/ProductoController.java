package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.model.Tipo;
import com.sw2parcial.backend.repository.ProductoRepository;
import com.sw2parcial.backend.repository.TipoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private TipoRepository tipoRepository;

    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        Integer tipoId = producto.getTipo().getId();
        Tipo tipoReal = tipoRepository.findById(tipoId)
                .orElseThrow(() -> new RuntimeException("Tipo no encontrado con id: " + tipoId));
        producto.setTipo(tipoReal);

        return productoRepository.save(producto);
    }

    @GetMapping("/{id}")
    public Producto obtenerPorId(@PathVariable Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @RequestBody Producto detalles) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto != null) {
            producto.setNombre(detalles.getNombre());
            producto.setPrecioVenta(detalles.getPrecioVenta());
            producto.setImagen(detalles.getImagen());
            producto.setStock(detalles.getStock());
            producto.setDescripcion(detalles.getDescripcion());
            producto.setTipo(detalles.getTipo());
            return productoRepository.save(producto);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoRepository.deleteById(id);
    }
    // Filtro por nombre y tipo
    @GetMapping("/buscar/nombre/{nombre}")
    public List<Producto> buscarPorNombre(@PathVariable String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    @GetMapping("/buscar/tipo/{nombre}")
    public List<Producto> buscarPorTipo(@PathVariable String nombre) {
        return productoRepository.findByTipoNombreContainingIgnoreCase(nombre);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path ruta = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
            Files.copy(file.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("ruta", "/uploads/" + nombreArchivo); // Importante que comience con "/"
            return ResponseEntity.ok(respuesta);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al subir imagen");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
