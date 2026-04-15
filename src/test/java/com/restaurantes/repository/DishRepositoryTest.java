package com.restaurantes.repository;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
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

    Restaurant restaurant1;
    Restaurant restaurant2;
    @BeforeEach
    void setUp() {
        // dos restaurantes
        restaurant1 = Restaurant.builder().name("Restaurante 1").active(true).build();
        restaurant2 = Restaurant.builder().name("Restaurante 2").active(true).build();
        restaurantRepository.saveAll(List.of(restaurant1, restaurant2));

        // añadir precio a Dish, poner precios desordenados
        var plato1 = Dish.builder().name("Plato 1").price(92.0).restaurant(restaurant1).build();
        var plato2 = Dish.builder().name("Plato 2").price(73.0).restaurant(restaurant1).build();
        var plato3 = Dish.builder().name("Plato 3").price(6.0).restaurant(restaurant2).build();
        var plato4 = Dish.builder().name("Plato 4").price(12.0).restaurant(restaurant2).build();
        var plato5 = Dish.builder().name("Plato 5").price(45.0).restaurant(restaurant2).build();
        var plato6 = Dish.builder().name("Plato 6").price(3.99).restaurant(restaurant2).build();
        dishRepository.saveAll(List.of(plato1, plato2, plato3, plato4, plato5, plato6));
    }

    @Test
    void findByRestaurant_Id() {
        List<Dish> platos = dishRepository.findByRestaurant_Id(restaurant1.getId());
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