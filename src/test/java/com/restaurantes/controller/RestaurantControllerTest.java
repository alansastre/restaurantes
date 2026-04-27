package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
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
@Transactional // deshace los cambios al final de cada test para no afectar al siguiente test
class RestaurantControllerTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

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
        // crear platos demo si se fueran a usar en varios métodos de test

    }

    @Test
    void restaurantsFull() throws Exception {
        // invocar endpoint http://localhost:8080/restaurants
        // se lanza una petición HTTP GET al controlador /restaurants
        // y verificamos que devuelve un status 200 OK
         mockMvc.perform(get("/restaurants"))
                 .andExpect(status().isOk())
                 .andExpect(view().name("restaurants/restaurant-list"))
                 .andExpect(model().attributeExists("restaurants"))
                 .andExpect(model().attribute("restaurants", hasSize(3)));
    }
    @Test
    void restaurantsEmpty() throws Exception {

        restaurantRepository.deleteAll();

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/restaurant-list"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(model().attribute("restaurants", hasSize(0)));
    }

    @Test
    void restaurantDetailIsPresentTrue() throws Exception {

        Restaurant restaurant = restaurantRepository.findAll().getFirst();
        Long restaurantId = restaurant.getId();


        mockMvc.perform(get("/restaurants/" + restaurantId))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurants/restaurant-detail"))
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

    // restaurantes con platos

    @Test
    void restaurantDetailWithDishes() throws Exception {

        // Crear platos y asignarlos a dos restaurantes
        List<Restaurant> restaurants = restaurantRepository.findAll();
        Restaurant restaurant1 = restaurants.get(0);
        Restaurant restaurant2 = restaurants.get(1);

        dishRepository.saveAll(List.of(
                Dish.builder().name("Plato 1").restaurant(restaurant1).build(),
                Dish.builder().name("Plato 2").restaurant(restaurant1).build(),

                Dish.builder().name("Plato 3").restaurant(restaurant2).build(),
                Dish.builder().name("Plato 4").restaurant(restaurant2).build(),
                Dish.builder().name("Plato 5").restaurant(restaurant2).build()
        ));

        // Comprobar que el detalle del restaurante 1 muestra sus platos
        mockMvc.perform(get("/restaurants/" + restaurant1.getId()))
                .andExpect(model().attribute("dishes", hasSize(2)));

        mockMvc.perform(get("/restaurants/" + restaurant2.getId()))
                .andExpect(model().attribute("dishes", hasSize(3)));

    }


}