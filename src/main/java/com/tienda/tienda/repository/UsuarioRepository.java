package com.tienda.tienda.repository;

import com.tienda.tienda.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameAndPassword(String username, String password);

    Optional<Usuario> findByUsername(String username);
}