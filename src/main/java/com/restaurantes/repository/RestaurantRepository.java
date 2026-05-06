package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByActiveTrue();
    Optional<Restaurant> findByIdAndActiveTrue(Long id);


    @Query("""
        select r from Restaurant r
        where r.active = true
        and (:price IS NULL OR r.averagePrice <= :price)
        """)
    List<Restaurant> findActiveFiltering(@Param("price") Double price);


}

