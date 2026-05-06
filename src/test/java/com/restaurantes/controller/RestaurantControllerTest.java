package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.FoodType;
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

    Restaurant restaurantToDeactivate;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        restaurantRepository.saveAll(List.of(
           Restaurant.builder().name("KFC").averagePrice(15.5).foodType(FoodType.SPANISH).build(),
           Restaurant.builder().name("MacDonals Burguer").averagePrice(8.5).foodType(FoodType.SPANISH).build(),
           Restaurant.builder().name("BurguerKing").averagePrice(7.5).foodType(FoodType.SPANISH).build(),
           Restaurant.builder().name("Tagliatella Pizza").averagePrice(29.99).foodType(FoodType.SPANISH).build(),
           Restaurant.builder().name("Sitio Pijo").averagePrice(60.5).foodType(FoodType.JAPANESE).build(),
           Restaurant.builder().name("Sitio Ultra Pijo").averagePrice(80.5).foodType(FoodType.MEXICAN).build()
        ));
        restaurantToDeactivate = restaurantRepository.save(
                Restaurant.builder().active(true).averagePrice(90d).name("El bar de Moe").build()
        );
    }
    @Test void filterRestaurantsByPrice() throws Exception {
        mockMvc.perform(get("/restaurants").param("price", "30"))
                .andExpect(model().attribute("restaurants", hasSize(4)));
    }
    @Test void filterRestaurantsByTitle() throws Exception {
        mockMvc.perform(get("/restaurants").param("title", "burguer"))
                .andExpect(model().attribute("restaurants", hasSize(2)));
    }
    @Test void filterRestaurantsByFoodType() throws Exception {
        mockMvc.perform(get("/restaurants").param("foodType", "MEXICAN"))
                .andExpect(model().attribute("restaurants", hasSize(1)));
    }
    @Test void filterRestaurantsByPriceAndTitle() throws Exception {
        mockMvc.perform(get("/restaurants").param("title", "burguer").param("price", "8"))
                .andExpect(model().attribute("restaurants", hasSize(1)));
    }



    @Test
    void deactivateRestaurant() throws Exception {
        assertTrue(restaurantToDeactivate.getActive());

        Long id = restaurantToDeactivate.getId();

        mockMvc.perform(get("/restaurants/deactivate/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/restaurants"));

        // traer restaurant de base de datos y comprobar que active es false
        Restaurant restaurantDB = restaurantRepository.findById(id).orElseThrow();
        assertFalse(restaurantDB.getActive());
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


    @Test
    void createNewRestaurante() throws Exception {

        // count restaurantes
        long before = restaurantRepository.count();

        // mockmvc perform enviar restaurante nuevo a controller
        mockMvc.perform(post("/restaurants")
                .param("name", "Restaurante Test")
                .param("averagePrice", "20.5")
                .param("foodType", "SPANISH")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/restaurants"));

        // count restaurantes tiene que haber un nuevo restaurant en base de datos
        long after = restaurantRepository.count();

        assertEquals(before + 1, after);
    }
}