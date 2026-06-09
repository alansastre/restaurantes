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
        mockMvc.perform(
                        get("/api/v1/restaurants/" +  restaurant1.getId() + "/dishes")
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].restaurant.name", everyItem(is("restaurante1"))))
                .andExpect(jsonPath("$[?(@.restaurant.id == " + restaurant1.getId() + ")]", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name == 'plato3')]", hasSize(0)));
    }

    @Test
    void create_OK() throws Exception {
        String dishJSON = String.format("""
                {
                  "name": "nuevoplato",
                  "description": "nuevoplato",
                  "price": 13.3,
                  "active": true,
                  "imageUrl": "string",
                  "type": "STARTER",
                  "restaurant": {
                    "id": %d
                  }
                }
               """, restaurant1.getId());

        mockMvc.perform(
                        post("/api/v1/dishes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dishJSON)
                ).andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("nuevoplato"))
                .andExpect(jsonPath("$.description").value("nuevoplato"))
                .andExpect(jsonPath("$.restaurant.id").value(restaurant1.getId()));
    }


    @Test
    void update_complete_OK() throws Exception {
        // no ponemos price para verificar que el PUT lo cambia a null
        String dishJSON = String.format("""
                    {
                      "id": %d,
                      "name": "Plato1 editado",
                      "description": "Plato1 editado",
                      "active": false,
                      "imageUrl": "/images/plato.png",
                      "type": "DESSERT",
                      "restaurant": {
                        "id": %d
                      }
                    }
               """, plato1.getId(), restaurant2.getId());

        mockMvc.perform(
                        put("/api/v1/dishes/" + plato1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dishJSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plato1.getId()))
                .andExpect(jsonPath("$.name").value("Plato1 editado"))
                .andExpect(jsonPath("$.description").value("Plato1 editado"))
                .andExpect(jsonPath("$.price").value(nullValue()))
                .andExpect(jsonPath("$.type").value(DishType.DESSERT.name()))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.imageUrl").value("/images/plato.png"))
                .andExpect(jsonPath("$.restaurant.id").value(restaurant2.getId()));
    }

    @Test
    void patchPartial_OK() throws Exception{
        String dishJSON = """
                {
                  "price": 9999
               }
               """;

        mockMvc.perform(
                        patch("/api/v1/dishes/" + plato1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dishJSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(plato1.getId()))
                .andExpect(jsonPath("$.name").value(plato1.getName()))
                .andExpect(jsonPath("$.type").value(plato1.getType().name()))
                .andExpect(jsonPath("$.description").value(plato1.getDescription()))
                .andExpect(jsonPath("$.price").value(9999d))
                .andExpect(jsonPath("$.active").value(plato1.getActive()))
                .andExpect(jsonPath("$.restaurant.name").value(plato1.getRestaurant().getName()));
    }

    @Test
    void deleteDish() throws Exception {

        mockMvc.perform(delete("/api/v1/dishes/" + plato1.getId()))
                .andExpect(status().isNoContent()); // 204
    }
}
