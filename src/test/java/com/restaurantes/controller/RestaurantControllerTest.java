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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        // se lanza una petición HTTP GET al controlador /restaurants
        // y verificamos que devuelve un status 200 OK
         mockMvc.perform(get("/restaurants"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("restaurant-list"))
                 .andExpect(model().attributeExists("restaurants"))
                 .andExpect(model().attribute("restaurants", hasSize(3)));
    }
    @Test
    void restaurantsEmpty() throws Exception {

        restaurantRepository.deleteAll();

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant-list"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attribute("restaurants", hasSize(0)));
    }

    @Test
    void restaurantDetailIsPresentTrue() throws Exception {

        Restaurant restaurant = restaurantRepository.findAll().getFirst();
        Long restaurantId = restaurant.getId();


        mockMvc.perform(get("/restaurants/" + restaurantId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant-detail"))
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attributeExists("dishes"))
                .andExpect(model().attribute("restaurant", hasProperty("id", is(restaurantId))))
                .andExpect(model().attribute("restaurant", hasProperty("name", is(restaurant.getName()))));
    }

    @Test
    void restaurantDetailIsPresentFalse()  throws Exception{
        // buscar un restaurante que no exista y comprobar que hace un redirect

        Long idInexistente = 99999L;

        mockMvc.perform(get("/restaurants/" + idInexistente))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));
    }

}