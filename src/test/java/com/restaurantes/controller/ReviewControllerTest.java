package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.Review;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Activa Spring
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional // deshace los cambios al final de cada test para no afectar al siguiente test
class ReviewControllerTest {

    @Autowired
    ReviewRepository reviewRepo;
    @Autowired
    DishRepository dishRepo;
    @Autowired
    RestaurantRepository restaurantRepo;

    @Autowired
    MockMvc mockMvc;

    Restaurant restaurant;
    Dish dish;
    Review review1;

    @BeforeEach
    void setUp() {
        review1 = reviewRepo.save(Review.builder().title("OK").rating(5).build());
        dish = dishRepo.save(Dish.builder().name("Plato1").price(10d).build());
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante").build());
    }
    @Test
    void createReviewDish() throws Exception {
        long countBefore = reviewRepo.count();
        mockMvc.perform(
                post("/reviews")
                        .param("title", "OK")
                        .param("rating", "5")
                        .param("content", "OKOK")
                        .param("dish", dish.getId().toString())

                )
                .andExpect(status().is3xxRedirection());
        assertEquals(countBefore + 1, reviewRepo.count());
        Review reviewCreated = reviewRepo.findAll().getLast();
        assertEquals("OK",  reviewCreated.getTitle());
        assertEquals(5,  reviewCreated.getRating());
        assertEquals("OKOK",  reviewCreated.getContent());
        assertNotNull(reviewCreated.getDish());
        assertEquals(dish.getId(),  reviewCreated.getDish().getId());
    }

    @Test
    void createReviewRestaurant() throws Exception{
        long countBefore = reviewRepo.count();
        mockMvc.perform(
                        post("/reviews")
                                .param("title", "OK")
                                .param("rating", "5")
                                .param("content", "OKOK")
                                .param("restaurant", restaurant.getId().toString())

                )
                .andExpect(status().is3xxRedirection());
        assertEquals(countBefore + 1, reviewRepo.count());
        Review reviewCreated = reviewRepo.findAll().getLast();
        assertEquals("OK",  reviewCreated.getTitle());
        assertEquals(5,  reviewCreated.getRating());
        assertEquals("OKOK",  reviewCreated.getContent());
        assertNotNull(reviewCreated.getRestaurant());
        assertEquals(restaurant.getId(),  reviewCreated.getRestaurant().getId());
    }

    @Test
    @DisplayName("Navegar hacia la pantalla de formulario de Review para crear una nueva")
    void navigateToNew() throws Exception {
        mockMvc.perform(
                get("/reviews/new")
                .param("restaurantId", restaurant.getId().toString())
        ).andExpect(view().name("reviews/review-form"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("review"))
        .andExpect(model().attribute("review", hasProperty("restaurant", hasProperty("id", is(restaurant.getId())))));

        mockMvc.perform(
                        get("/reviews/new")
                                .param("dishId", dish.getId().toString())
                ).andExpect(view().name("reviews/review-form"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("review"))
                .andExpect(model().attribute("review", hasProperty("dish", hasProperty("id", is(dish.getId())))));

    }


    @Test
    void deleteReview() throws Exception {

        // /reviews/delete/{id}
        Long id = review1.getId();
        assertTrue(reviewRepo.existsById(id));

        mockMvc.perform(get("/reviews/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                .andExpect(flash().attribute("message", "Borrado exitosamente"));


        assertFalse(reviewRepo.existsById(id));
    }



}