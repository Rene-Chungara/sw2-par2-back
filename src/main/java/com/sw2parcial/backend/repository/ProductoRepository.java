package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByTipoNombreContainingIgnoreCase(String nombreTipo);
}