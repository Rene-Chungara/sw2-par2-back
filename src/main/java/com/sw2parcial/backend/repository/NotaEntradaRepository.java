package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.NotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaEntradaRepository extends JpaRepository<NotaEntrada, Integer> {
    List<NotaEntrada> findByProveedorNombreContainingIgnoreCase(String nombre);
}
