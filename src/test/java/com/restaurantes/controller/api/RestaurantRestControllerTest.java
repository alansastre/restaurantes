package com.restaurantes.controller.api;

import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.FoodType;
import com.restaurantes.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class RestaurantRestControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    RestaurantRepository restaurantRepo;

    Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = restaurantRepo.save(Restaurant.builder()
                .name("Restaurante Test 1")
                .averagePrice(10.99)
                .foodType(FoodType.SPANISH)
                .build());
    }

    @Test
    void findAll() throws Exception{
        mockMvc.perform(get("/api/v1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
//                .andExpect(jsonPath("$[0].name", is("Restaurante Test 1")))
                .andExpect(jsonPath("$[?(@.name == 'Restaurante Test 1')]", hasSize(1)))
                .andExpect(jsonPath("$[?(@.name == 'Restaurante Test 1')].averagePrice", contains(10.99)))
                .andExpect(jsonPath("$[?(@.name == 'Restaurante Test 1')].foodType", contains(FoodType.SPANISH.name())));
    }

    @Test
    void findOne_OK() throws Exception{
        mockMvc.perform(get("/api/v1/restaurants/" +  restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(restaurant.getId()))
                .andExpect(jsonPath("$.name").value(restaurant.getName()))
                .andExpect(jsonPath("$.foodType").value(restaurant.getFoodType().name()));
    }

    @Test
    void findOne_NotFound() throws Exception{
        mockMvc.perform(get("/api/v1/restaurants/99999"))
                .andExpect(status().isNotFound());
    }
}