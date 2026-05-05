package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByActiveTrue();
    Optional<Restaurant> findByIdAndActiveTrue(Long id);
}

