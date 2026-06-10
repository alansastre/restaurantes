package com.restaurantes.integration;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * El mismo tipo de test que DishRepositoryTest (que corre en H2), pero contra PostgreSQL real.
 *
 * Clave: @AutoConfigureTestDatabase(replace = NONE) para que @DataJpaTest NO sustituya la BD
 * por una H2 embebida y use la del contenedor (que llega via @ServiceConnection de la clase base).
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DishRepositoryPostgresTest extends AbstractPostgresIT {

    @Autowired RestaurantRepository restaurantRepository;
    @Autowired DishRepository dishRepository;
    @Autowired DataSource dataSource;

    @Test
    void estaCorriendoContraPostgres_noH2() throws Exception {
        try (Connection c = dataSource.getConnection()) {
            assertEquals("PostgreSQL", c.getMetaData().getDatabaseProductName());
        }
    }

    @Test
    void findByPriceLessThanEqual() {
        Restaurant r = restaurantRepository.save(
                Restaurant.builder().name("Restaurante 1").active(true).build());

        dishRepository.saveAll(List.of(
                Dish.builder().name("Barato 1").price(6.0).restaurant(r).build(),
                Dish.builder().name("Barato 2").price(9.99).restaurant(r).build(),
                Dish.builder().name("Caro").price(50.0).restaurant(r).build()));

        List<Dish> baratos = dishRepository.findByPriceLessThanEqual(10.0);

        assertEquals(2, baratos.size());
        for (Dish d : baratos) assertTrue(d.getPrice() <= 10.0);
    }
}
