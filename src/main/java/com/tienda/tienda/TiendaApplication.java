package com.tienda.tienda;

import com.tienda.tienda.entity.Usuario;
import com.tienda.tienda.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TiendaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiendaApplication.class, args);
    }

    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository repository) {
        return args -> {
            if (repository.findByUsername("admininicial").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admininicial");
                admin.setPassword("admin123456");
                repository.save(admin);
            }
        };
    }

}
