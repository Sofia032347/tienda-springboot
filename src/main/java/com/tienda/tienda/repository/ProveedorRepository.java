package com.tienda.tienda.repository;

import com.tienda.tienda.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    List<Proveedor> findByNombreProveedorContainingIgnoreCaseOrNitContainingIgnoreCaseOrCiudadContainingIgnoreCase(
            String nombre, String nit, String ciudad);
}