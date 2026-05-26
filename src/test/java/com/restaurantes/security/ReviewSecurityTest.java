package com.restaurantes.security;
import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.Review;
import com.restaurantes.model.User;
import com.restaurantes.model.enums.Role;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import com.restaurantes.repository.UserRepository;
import com.restaurantes.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest // Activa Spring
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional
public class ReviewSecurityTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void list_anonymous_200() throws Exception {
        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk());
    }
    @Test
    void new_anonymous_401() throws Exception {
        mockMvc.perform(get("/reviews/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    @Test
    void delete_anonymous_401() throws Exception {
        mockMvc.perform(get("/reviews/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    // detail_anonymous_200
    // post_anonymous_401

    // new_user_200

    // Opción 1 para pasar un usuario mock: .with(user("pepe").roles("USER"))
    @Test
    void new_user_200 () throws Exception {
        mockMvc.perform(
                get("/reviews/new")
                        .with(user("pepe").roles("USER"))
                ).andExpect(status().isOk());
    }

    // Opción 2 para pasar un usuario mock: @WithMockUser
    @Test
    @WithMockUser(username = "pepe", roles = {"USER"})
    void new_user_200_con_anotacion () throws Exception {
        mockMvc.perform(
                get("/reviews/new")
        ).andExpect(status().isOk());
    }

    // post_user_200
    @Test
    @DisplayName("POST /reviews siendo ROLE_USER")
    void post_user_200 () throws Exception {
        long countBefore = reviewRepository.count();
        mockMvc.perform(post("/reviews")
                        .param("title", "OK")
                        .param("rating", "5")
                        .with(user("pepe").roles("USER"))
                        .with(csrf())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/reviews"));
        assertEquals(countBefore + 1, reviewRepository.count());
    }
    @Test
    @DisplayName("POST /reviews siendo ANONYMOUS")
    void post_anonymous_403 () throws Exception {
        long countBefore = reviewRepository.count();
        mockMvc.perform(post("/reviews")
                        .param("title", "OK")
                        .param("rating", "5")
                ).andExpect(status().isForbidden()); //403
        assertEquals(countBefore, reviewRepository.count());
    }



}
