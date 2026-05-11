package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class DishControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepo;
    @Autowired RestaurantRepository restaurantRepo;
    Restaurant restaurant;
    Dish dish;
    @Autowired
    private DishRepository dishRepository;

    @BeforeEach
    void setUp() {
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante1").build());
        dish = dishRepo.save(Dish.builder().name("plato1").restaurant(restaurant).build());
    }
    @Test void create() throws Exception {
        long countBefore = dishRepo.count();

        // mockmvc perform post /dishes
        mockMvc.perform(post("/dishes")
                .param("name", "plato name test")
                .param("price", "10")
                .param("description", "plato description test")
                .param("type", DishType.DESSERT.toString())
                .param("restaurant", restaurant.getId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/dishes/*"));

        assertEquals(countBefore + 1, dishRepo.count());
        Dish dish = dishRepository.findAll().getLast();
        assertEquals("plato name test", dish.getName());
        assertEquals(10d, dish.getPrice());
        assertEquals("plato description test", dish.getDescription());
        assertEquals(DishType.DESSERT, dish.getType());
        assertEquals(restaurant.getId(), dish.getRestaurant().getId());

    }
    @Test
    void edit() {
        // save

        // mockmvc perform post cambiando nombre precio restaurante apuntando al mismo id /dishes

        // findById y comprobar datos
        // count no debe aumentar
        Assertions.fail("Pendiente test editar Plato existente");
    }

//    @Test
//    void list() {
//        Assertions.fail("Pendiente test detail");
//    }
//    @Test
//    void detail() {
//        Assertions.fail("Pendiente test detail");
//    }
}