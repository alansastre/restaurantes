package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void guardarRestaurante() {

        // INSERT INTO restaurantes
         Restaurant restaurant = new Restaurant();
        // RestaurantRepository repo = new RestaurantRepository(); // No se puede porque es una interface no una class

//        Restaurant restaurant = Restaurant.builder().build();
        restaurant.setName("Restaurante");
        restaurant.setAveragePrice(20.0);
        restaurant = restaurantRepository.save(restaurant);

        assertNotNull(restaurant.getId());
        assertEquals("Restaurante", restaurant.getName());

    }
}