package com.restaurantes.repository;

import com.restaurantes.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class DishRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;



    @BeforeEach
    void setUp() {
        // dos restaurantes

        // añadir precio a Dish, poner precios desordenados

    }

    @Test
    void findByRestaurant_Id() {
        List<Dish> platos = dishRepository.findByRestaurant_Id(1L);
        assertEquals(2, platos.size());
    }

    @Test
    void findByPriceLessThanEqual() {
        List<Dish> platos = dishRepository.findByPriceLessThanEqual(10.0);
        assertEquals(2, platos.size());
        for (Dish dish : platos)
            assertTrue(dish.getPrice() <= 10.0);
    }

    @Test
    void findOrderedByPriceAsc() {
        List<Dish> platos = dishRepository.findOrderedByPriceAsc();
        assertEquals(6, platos.size());
        for (int i = 0; i < platos.size() - 1; i++) {
            Double precioActual = platos.get(i).getPrice();
            Double precioSiguiente = platos.get(i + 1).getPrice();
            assertTrue(precioActual <= precioSiguiente);
            // assertTrue( platos.get(i).getPrice() <= platos.get(i + 1).getPrice() );
        }
    }
}