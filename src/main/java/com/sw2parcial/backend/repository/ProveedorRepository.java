package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByNombreContainingIgnoreCase(String nombre);
    List<Proveedor> findByOrigenContainingIgnoreCase(String origen);
}
