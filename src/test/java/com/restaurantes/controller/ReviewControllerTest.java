package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.Review;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Activa Spring
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional // deshace los cambios al final de cada test para no afectar al siguiente test
class ReviewControllerTest {

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    MockMvc mockMvc;

    Review review1;

    @BeforeEach
    void setUp() {
        review1 = reviewRepo.save(Review.builder().title("OK").rating(5).build());

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