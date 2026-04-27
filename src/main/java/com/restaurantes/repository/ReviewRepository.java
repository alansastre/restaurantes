package com.restaurantes.repository;

import com.restaurantes.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRestaurant_IdOrderByCreationDateDesc(Long id);

    List<Review> findByDish_IdOrderByCreationDateDesc(Long id);


}