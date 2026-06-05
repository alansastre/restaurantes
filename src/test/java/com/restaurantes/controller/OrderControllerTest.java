package com.restaurantes.controller;


import com.restaurantes.model.*;
import com.restaurantes.model.enums.OrderStatus;
import com.restaurantes.model.enums.Role;
import com.restaurantes.repository.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

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
@Disabled
@SpringBootTest
@AutoConfigureMockMvc // desactiva Security
@Transactional
public class OrderControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired RestaurantRepository restaurantRepo;
    @Autowired DishRepository dishRepo;
    @Autowired OrderRepository orderRepo;
    @Autowired OrderLineRepository orderLineRepo;
    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder passwordEncoder;

    Restaurant restaurant;
    Dish ensalada, lentejas, flan;
    Order pedidoJuanito;
    OrderLine lineaEnsalada, lineaLentejas, lineaFlan;
    User admin;

    @BeforeEach
    void setUp() {
        admin = userRepo.save(User.builder().email("admin@gmail.com").username("admin").password(passwordEncoder.encode("admin")).role(Role.ROLE_ADMIN).build());

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
        mockMvc.perform(get("/orders").with(user(admin)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/order-list"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attribute("orders", hasSize(greaterThanOrEqualTo(1))));
    }
    @Test
    @DisplayName("GET /orders/{id}")
    void detail() throws Exception {
        mockMvc.perform(
                    get(
                            "/orders/" + pedidoJuanito.getId()
                    ).with(user("pepe").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("orders/order-detail"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("orderLines"))
                .andExpect(model().attributeExists("dishes"))
                .andExpect(model().attribute("orderLines", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(model().attribute("dishes", hasSize(greaterThanOrEqualTo(3))));
    }

    @WithMockUser(username = "pepe", roles = {"USER"})
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

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    @DisplayName("POST /orders")
    void createOrder() throws Exception{
        mockMvc.perform(
                post("/orders").with(csrf())
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

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    @DisplayName("POST /orders/{orderId}/lines?dishId={id}")
    void createLine() throws Exception{

        long countLines = orderLineRepo.count();

        mockMvc.perform(
                post("/orders/" + pedidoJuanito.getId() + "/lines").with(csrf())
                .param("dishId", flan.getId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/orders/*"));

        assertEquals(countLines + 1, orderLineRepo.count());

        Order recargado = orderRepo.findById(pedidoJuanito.getId()).orElseThrow();
        assertEquals(OrderStatus.IN_PROGRESS, recargado.getStatus());
        assertEquals(30d,  recargado.getTotalPrice());

        OrderLine lineaFlanRecargada = orderLineRepo.findAll().getLast();
        assertEquals(1, lineaFlanRecargada.getQuantity());
        assertEquals(flan.getId(), lineaFlanRecargada.getDish().getId());
        assertEquals(pedidoJuanito.getId(), lineaFlanRecargada.getOrder().getId());


    }
    @Test
    @DisplayName("GET /orders/{orderId}/lines/{lineId}/delete")
    void deleteLine() throws Exception{}
    @Test
    @DisplayName("POST /orders/{orderId}/lines/{lineId}?quantity=2")
    void updateLine() throws Exception{}

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    @DisplayName("GET /orders/{orderId}/finish?tip=0")
    void finishOrder() throws Exception {

        mockMvc.perform(
                get("/orders/" + pedidoJuanito.getId() + "/finish")
                .param("tip", "1.34")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/orders/" + pedidoJuanito.getId()));

        Order finalizado =  orderRepo.findById(pedidoJuanito.getId()).orElseThrow();
        assertEquals(OrderStatus.FINISHED, finalizado.getStatus());
        assertEquals(1.34d , finalizado.getTip());
        assertEquals(25d,   finalizado.getTotalPrice());
    }
}
