package com.restaurantes.controller;

import com.restaurantes.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc // habilitar y configurar MockMvc para poder usarlo
class RestaurantControllerTest {

    // restaurant repository para crear datos demo
    @Autowired
    RestaurantRepository restaurantRepository;

    // Utilidad de Testing para lanzar peticiones a controladores en test
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // crear datos demo
    }

    @Test
    void restaurantsFull() {
        // invocar endpoint http://localhost:8080/restaurants
        // mockMvc.perform();
    }
    @Test
    void restaurantsEmpty() {
        // invocar endpoint http://localhost:8080/restaurants
        // mockMvc.perform();
    }


}