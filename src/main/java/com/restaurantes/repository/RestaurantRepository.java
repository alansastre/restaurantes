package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.FoodType;
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
        and (:title IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :title, '%')))
        and (:foodType IS NULL OR r.foodType = :foodType)
        """)
    List<Restaurant> findActiveFiltering(
            @Param("price") Double price,
            @Param("title") String title,
            @Param("foodType") FoodType foodType
    );


}

