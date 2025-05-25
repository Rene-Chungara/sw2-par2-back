package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.DetalleNotaEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleNotaEntradaRepository extends JpaRepository<DetalleNotaEntrada, Integer> {
    List<DetalleNotaEntrada> findByNotaEntradaId(Integer notaEntradaId);
}
