package com.restaurantes.repository;

import com.restaurantes.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // calculate total price based on order lines
    // findByRestuaur....
    // List<Order> findByRestaur2anId();
}