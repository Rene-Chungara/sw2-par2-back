package com.sw2parcial.backend.controller;

import com.sw2parcial.backend.model.DetalleNotaEntrada;
import com.sw2parcial.backend.model.NotaEntrada;
import com.sw2parcial.backend.model.Producto;
import com.sw2parcial.backend.repository.DetalleNotaEntradaRepository;
import com.sw2parcial.backend.repository.NotaEntradaRepository;
import com.sw2parcial.backend.repository.ProductoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sw2parcial.backend.dto.DetalleNotaEntradaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-nota-entrada")
@CrossOrigin(origins = "*")
public class DetalleNotaEntradaController {

    @Autowired
    private DetalleNotaEntradaRepository detalleNotaEntradaRepository;

    @Autowired
    private NotaEntradaRepository notaEntradaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    public List<DetalleNotaEntrada> listarTodos() {
        return detalleNotaEntradaRepository.findAll();
    }

    @PostMapping
    public DetalleNotaEntrada crear(@RequestBody DetalleNotaEntradaDTO dto) {
        NotaEntrada notaEntrada = notaEntradaRepository.findById(dto.notaEntradaId)
                .orElseThrow(() -> new RuntimeException("NotaEntrada no encontrada"));

        Producto producto = productoRepository.findById(dto.productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        DetalleNotaEntrada detalle = new DetalleNotaEntrada();
        detalle.setNotaEntrada(notaEntrada);
        detalle.setProducto(producto);
        detalle.setCantidad(dto.cantidad);
        detalle.setCostoUnitario(dto.costoUnitario);

        return detalleNotaEntradaRepository.save(detalle);
}

    @GetMapping("/{id}")
    public DetalleNotaEntrada obtenerPorId(@PathVariable Integer id) {
        return detalleNotaEntradaRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public DetalleNotaEntrada actualizar(@PathVariable Integer id, @RequestBody DetalleNotaEntrada datos) {
        DetalleNotaEntrada detalle = detalleNotaEntradaRepository.findById(id).orElse(null);
        if (detalle != null) {
            detalle.setCostoUnitario(datos.getCostoUnitario());
            detalle.setCantidad(datos.getCantidad());
            detalle.setNotaEntrada(datos.getNotaEntrada());
            detalle.setProducto(datos.getProducto());
            return detalleNotaEntradaRepository.save(detalle);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        detalleNotaEntradaRepository.deleteById(id);
    }

    @GetMapping("/nota/{notaEntradaId}")
    public List<DetalleNotaEntrada> listarPorNota(@PathVariable Integer notaEntradaId) {
        return detalleNotaEntradaRepository.findByNotaEntradaId(notaEntradaId);
    }
}
