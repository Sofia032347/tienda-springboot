package com.tienda.tienda.repository;

import com.tienda.tienda.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Map;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query(value = "SELECT c.cedula AS cedula, c.nombre_completo AS nombre, SUM(v.total_con_iva) AS total " +
            "FROM venta v " + // CAMBIADO: de 'ventas' a 'venta'
            "INNER JOIN clientes c ON v.cedula_cliente = c.cedula " +
            "GROUP BY c.cedula, c.nombre_completo", nativeQuery = true)
    List<Map<String, Object>> obtenerVentasPorCliente();
}