package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.DetalleVenta;
import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

        // Buscar detalles por venta
        List<DetalleVenta> findByVenta(Venta venta);

        // Buscar detalles por producto
        List<DetalleVenta> findByProducto(Producto producto);

        // Buscar detalles por ID de venta
        List<DetalleVenta> findByVentaId(Integer ventaId);

        // Calcular cantidad total vendida de un producto
        @Query("SELECT SUM(dv.cantidad) FROM DetalleVenta dv WHERE dv.producto = :producto")
        Integer calcularCantidadTotalVendida(@Param("producto") Producto producto);

        // Obtener productos más vendidos
        @Query("SELECT dv.producto, SUM(dv.cantidad) as total FROM DetalleVenta dv " +
                        "GROUP BY dv.producto ORDER BY total DESC")
        List<Object[]> findProductosMasVendidos();

        // Calcular ingresos por producto
        @Query("SELECT dv.producto, SUM(dv.cantidad * dv.precioUnitario) as ingresos " +
                        "FROM DetalleVenta dv GROUP BY dv.producto ORDER BY ingresos DESC")
        List<Object[]> findIngresosPorProducto();
}