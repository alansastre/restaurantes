package com.restaurantes.repository;

import com.restaurantes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // registro: verificar si email o username están ocupados
    boolean existByUsername(String username);
    boolean existByEmail(String email);

    // login: buscar user
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);


}