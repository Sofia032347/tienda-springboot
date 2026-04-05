package com.tienda.tienda.repository;

import com.tienda.tienda.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNombreCompletoContainingIgnoreCaseOrCedulaContainingIgnoreCase(
            String nombre, String cedula);

    Optional<Cliente> findByCedula(String cedula);


}
