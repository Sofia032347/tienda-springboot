package com.tienda.tienda.repository;

import com.tienda.tienda.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    List<DetalleVenta> findByCodigoVenta(Long codigoVenta);
}