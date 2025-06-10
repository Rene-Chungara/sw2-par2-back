package com.sw2parcial.backend.repository;

import com.sw2parcial.backend.model.Venta;
import com.sw2parcial.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    // Buscar ventas por usuario
    List<Venta> findByUsuario(Usuario usuario);
    
    // Buscar ventas por estado
    List<Venta> findByEstado(String estado);
    
    // Buscar ventas por fecha
    List<Venta> findByFechaVenta(LocalDate fechaVenta);
    
    // Buscar ventas entre fechas
    List<Venta> findByFechaVentaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar ventas por canal de venta
    List<Venta> findByCanalVenta(String canalVenta);
    
    // Calcular total de ventas por usuario
    @Query("SELECT SUM(v.ventaTotal) FROM Venta v WHERE v.usuario = :usuario")
    Float calcularTotalVentasPorUsuario(@Param("usuario") Usuario usuario);
    
    // Obtener ventas con detalles
    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.detalleVentas WHERE v.id = :id")
    Venta findByIdWithDetalles(@Param("id") Integer id);
    
    // Ventas del día actual
    //@Query("SELECT v FROM Venta v WHERE DATE(v.fechaVenta) = CURRENT_DATE")
    //List<Venta> findVentasDelDia();
    @Query("SELECT v FROM Venta v WHERE v.fechaVenta = CURRENT_DATE")
    List<Venta> findVentasDelDia(); 

    @Query("SELECT v FROM Venta v WHERE v.usuario.telefono = :telefono")
    List<Venta> findByUsuarioTelefono(@Param("telefono") String telefono);

}