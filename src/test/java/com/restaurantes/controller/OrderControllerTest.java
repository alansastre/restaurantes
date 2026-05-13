package com.restaurantes.controller;


import com.restaurantes.model.Dish;
import com.restaurantes.model.Order;
import com.restaurantes.model.OrderLine;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.OrderStatus;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.OrderLineRepository;
import com.restaurantes.repository.OrderRepository;
import com.restaurantes.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/*
LOGICA DE HACER PEDIDOS EN UN RESTAURANTE

Como usuario cliente quiero poder iniciar un
pedido en un restaurante, y poder añadir/eliminar
platos al pedido y finalizar pedido.

OrderController
     * OK @GetMapping("orders")
     * OK @GetMapping("orders/{id}")
     * TODO @GetMapping("orders/new")
     * TODO @PostMapping("orders")
     * TODO @PostMapping("orders/{id}/lines")
     * TODO @GetMapping("orders/{orderId}/lines/{lineId}/delete")
     * TODO @PostMapping("orders/{orderId}/lines/{lineId}") quantity=4
     * TODO @GetMapping("orders/{id}/finish")
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired RestaurantRepository restaurantRepo;
    @Autowired DishRepository dishRepo;
    @Autowired OrderRepository orderRepo;
    @Autowired OrderLineRepository orderLineRepo;

    Restaurant restaurant;
    Dish ensalada, lentejas, flan;
    Order pedidoJuanito;
    OrderLine lineaEnsalada, lineaLentejas, lineaFlan;

    @BeforeEach
    void setUp() {
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante1").build());
        ensalada = dishRepo.save(Dish.builder().name("Ensalada").price(10d).restaurant(restaurant).build());
        lentejas = dishRepo.save(Dish.builder().name("Lentejas").price(15d).restaurant(restaurant).build());
        flan = dishRepo.save(Dish.builder().name("Flan").price(5d).restaurant(restaurant).build());

        pedidoJuanito = orderRepo.save(Order.builder().restaurant(restaurant).status(OrderStatus.PENDING).numPeople(2).build());
        lineaEnsalada = orderLineRepo.save(OrderLine.builder().dish(ensalada).order(pedidoJuanito).quantity(1).build());
        lineaLentejas = orderLineRepo.save(OrderLine.builder().dish(lentejas).order(pedidoJuanito).quantity(1).build());
    }
    @Test
    @DisplayName("GET /orders")
    void list() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/order-list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(greaterThanOrEqualTo(1))));
    }
    @Test
    @DisplayName("GET /orders/{id}")
    void detail() throws Exception {
        mockMvc.perform(get("/orders/" + pedidoJuanito.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/order-detail"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("orderLines"))
                .andExpect(model().attributeExists("dishes"))
                .andExpect(model().attribute("orderLines", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(model().attribute("dishes", hasSize(greaterThanOrEqualTo(3))));
    }
    @Test
    @DisplayName("GET /orders/new?restaurantId={id}")
    void newOrder() throws Exception {
        mockMvc.perform(
                get("/orders/new")
                .param("restaurantId", restaurant.getId().toString())
        ).andExpect(status().isOk())
        .andExpect(view().name("orders/order-form"))
        .andExpect(model().attributeExists("order"))
        .andExpect(model().attribute("order", hasProperty("restaurant", hasProperty("id", is(restaurant.getId())))));
    }
    @Test
    @DisplayName("POST /orders")
    void createOrder() throws Exception{
        mockMvc.perform(
                post("/orders")
                .param("tableNumber", "1")
                .param("numPeople", "2")
                .param("userSuggestions", "Alergia a todo")
                .param("restaurant", restaurant.getId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/orders/*"));

        Order creado = orderRepo.findAll().getLast();
        assertEquals(1,  creado.getTableNumber());
        assertEquals(2,  creado.getNumPeople());
        assertEquals("Alergia a todo",  creado.getUserSuggestions());
        assertEquals(restaurant.getId(),  creado.getRestaurant().getId());
    }
    @Test
    @DisplayName("POST /orders/{orderId}/lines?dishId={id}")
    void createLine() throws Exception{}
    @Test
    @DisplayName("GET /orders/{orderId}/lines/{lineId}/delete")
    void deleteLine() throws Exception{}
    @Test
    @DisplayName("POST /orders/{orderId}/lines/{lineId}?quantity=2")
    void updateLine() throws Exception{}
    @Test
    @DisplayName("GET /orders/{orderId}/finish?tip=0")
    void finishOrder() throws Exception {}
}
