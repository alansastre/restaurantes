package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Test de integración sin mocks
@AutoConfigureMockMvc
@Transactional
@SpringBootTest
public class DishControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepo;
    @Autowired RestaurantRepository restaurantRepo;

    Restaurant restaurant;
    Restaurant restaurant2;
    Dish dish;

    @Autowired
    private DishRepository dishRepository;

    @BeforeEach
    void setUp() {
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante1").build());
        restaurant2 = restaurantRepo.save(Restaurant.builder().name("Restaurante2").build());
        dish = dishRepo.save(Dish.builder().name("plato1").restaurant(restaurant).build());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void create() throws Exception {
        long countBefore = dishRepo.count();

        // mockmvc perform post /dishes
        mockMvc.perform(post("/dishes").with(csrf())
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void edit() throws Exception {
        Long dishId = dish.getId();
        long countBefore = dishRepo.count();
        mockMvc.perform(post("/dishes").with(csrf())
                .param("id",  dishId.toString())
                .param("name", "plato name test editado")
                .param("price", "9")
                .param("description", "plato description test editado")
                .param("type", DishType.MAIN_COURSE.toString())
                .param("restaurant", restaurant2.getId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dishes/" + dishId));

        assertEquals(countBefore, dishRepo.count());
        Dish editedDish = dishRepo.findById(dishId).orElseThrow();
        assertEquals("plato name test editado", editedDish.getName());
        assertEquals(9d, editedDish.getPrice());
        assertEquals("plato description test editado", editedDish.getDescription());
        assertEquals(DishType.MAIN_COURSE, editedDish.getType());
        assertEquals(restaurant2.getId(), editedDish.getRestaurant().getId());
    }

    @Test
    @DisplayName("GET /dishes")
    void dishes() throws Exception {
        // Realiza petición GET a /dishes y espera la vista correcta y que el modelo incluya el plato creado en setUp()
        mockMvc.perform(get("/dishes"))
                .andExpect(status().isOk())
                .andExpect(view().name("dishes/dish-list"))
                .andExpect(model().attributeExists("dishes"))
                .andExpect(model().attribute("dishes", hasItem(
                        allOf(
                                hasProperty("id", is(dish.getId())),
                                hasProperty("name", is("plato1"))
                        )
                )));
    }

    @Test
    @DisplayName("GET /dishes/{id}")
    void dishDetail () throws Exception {
        // Petición GET al detalle del plato creado en setUp()
        mockMvc.perform(get("/dishes/" + dish.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("dishes/dish-detail"))
                .andExpect(model().attributeExists("dish", "reviews"))
                .andExpect(model().attribute("dish", allOf(
                        hasProperty("id", is(dish.getId())),
                        hasProperty("name", is("plato1"))
                )));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("GET /dishes/new")
    void navigateToCreate() throws Exception {
        // GET al formulario de creación (requiere role ADMIN)
        mockMvc.perform(get("/dishes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("dishes/dish-form"))
                .andExpect(model().attributeExists("dish", "restaurants"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("GET /dishes/edit/{id}")
    void navigateToEdit() throws Exception {
        // GET al formulario de edición para el plato creado en setUp() (requiere role ADMIN)
        mockMvc.perform(get("/dishes/edit/" + dish.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("dishes/dish-form"))
                .andExpect(model().attributeExists("dish", "restaurants"))
                .andExpect(model().attribute("dish", hasProperty("id", is(dish.getId()))));
    }



}