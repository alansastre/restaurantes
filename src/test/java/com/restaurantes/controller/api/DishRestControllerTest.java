package com.restaurantes.controller.api;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.model.enums.FoodType;
import com.restaurantes.repository.DishRepository;
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
public class DishRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DishRepository dishRepo;
    @Autowired
    private RestaurantRepository restaurantRepo;

    Restaurant restaurant1;
    Restaurant restaurant2;
    Dish plato1;
    Dish plato2;
    Dish plato3;

    @BeforeEach
    void setUp() {
        restaurant1 = restaurantRepo.save(
                Restaurant.builder()
                        .name("restaurante1")
                        .build()
        );
        restaurant2 = restaurantRepo.save(
                Restaurant.builder()
                        .name("restaurante2")
                        .build()
        );
        plato1 = dishRepo.save(
                Dish.builder()
                        .name("plato1")
                        .restaurant(restaurant1)
                        .price(10.00)
                        .type(DishType.MAIN_COURSE)
                        .active(true)
                        .description("plato1")
                        .build()
        );
        plato2 = dishRepo.save(
                Dish.builder()
                        .name("plato2")
                        .restaurant(restaurant1)
                        .price(20.00)
                        .type(DishType.MAIN_COURSE)
                        .active(true)
                        .description("plato2")
                        .build()
        );
        plato3 = dishRepo.save(
                Dish.builder()
                        .name("plato3")
                        .restaurant(restaurant2)
                        .price(20.00)
                        .type(DishType.MAIN_COURSE)
                        .active(true)
                        .description("plato3")
                        .build()
        );
    }

    @Test
    void findAll() throws Exception{

        mockMvc.perform(
                get("/api/v1/dishes")
        ).andExpect(status().isOk())
    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    .andExpect(jsonPath("$").isArray())
    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))))
//                .andExpect(jsonPath("$[0].name", is("Restaurante Test 1")))
    .andExpect(jsonPath("$[?(@.name == 'plato1')]", hasSize(1)))
    .andExpect(jsonPath("$[?(@.name == 'plato1')].price", contains(10.00)))
    .andExpect(jsonPath("$[?(@.name == 'plato1')].type", contains(DishType.MAIN_COURSE.name())))
    .andExpect(jsonPath("$[?(@.name == 'plato1')].restaurant.name", contains("restaurante1")))
    .andExpect(jsonPath("$[?(@.name == 'plato2')].restaurant.name", contains("restaurante1")))
    .andExpect(jsonPath("$[?(@.name == 'plato3')].restaurant.name", contains("restaurante2")));
    }

    @Test
    void findById() throws Exception{
        mockMvc.perform(get("/api/v1/dishes/" +  plato1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(plato1.getId()))
                .andExpect(jsonPath("$.name").value(plato1.getName()))
                .andExpect(jsonPath("$.type").value(plato1.getType().name()))
                .andExpect(jsonPath("$.description").value(plato1.getDescription()))
                .andExpect(jsonPath("$.price").value(plato1.getPrice()))
                .andExpect(jsonPath("$.active").value(plato1.getActive()))
                .andExpect(jsonPath("$.restaurant.name").value(plato1.getRestaurant().getName()));
    }

    @Test // carta
    void findByRestaurant() throws Exception{

    }
}
