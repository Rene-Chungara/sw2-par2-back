package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrecioRepository extends JpaRepository<Precio, Integer> {
    List<Precio> findByProductoId(Integer productoId);
}
