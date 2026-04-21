package com.restaurantes.controller;

import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest // Activa Spring
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
class RestaurantControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        // crear restaurantes demo
        restaurantRepository.deleteAll();
        restaurantRepository.saveAll(List.of(
           Restaurant.builder().name("La taberna 1").averagePrice(20.5).build(),
           Restaurant.builder().name("La taberna 2").averagePrice(30.5).build(),
           Restaurant.builder().name("La taberna 3").averagePrice(40.5).build()
        ));

    }

    @Test
    void restaurantsFull() throws Exception {
        // invocar endpoint http://localhost:8080/restaurants
         mockMvc.perform(get("/restaurants"));
    }
    @Test
    void restaurantsEmpty() {
        // invocar endpoint http://localhost:8080/restaurants
        // mockMvc.perform();
    }


}