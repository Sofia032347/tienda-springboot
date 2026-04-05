package com.tienda.tienda.repository;

import com.tienda.tienda.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByNombreProductoContainingIgnoreCase(String nombre);

    boolean existsByNitProveedor(Long nitProveedor);

    Optional<Producto> findByCodigoProducto(Long codigoProducto);
}