package com.restaurantes.repository;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Order;
import com.restaurantes.model.OrderLine;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests de pedidos en base de datos")
class OrderRepositoryTest {

    // restaurant repository
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;

    // declarar datos para las pruebas
    Dish dish1, dish2;
    Restaurant restaurant1;
    Order order1;
    Order order2;
    @Autowired
    private DishRepository dishRepository;


    @BeforeEach
    void setUp() {
        // inicializar datos de prueba
        dish1 = dishRepository.save(Dish.builder().name("Plato 1").price(10.0).build());
        dish2 = dishRepository.save(Dish.builder().name("Plato 2").price(20.0).build());
        restaurant1 = restaurantRepository.save(Restaurant.builder().name("Restaurante 1").build());
        // crear y guardar un pedido en base de datos
        order1 = orderRepository.save(Order.builder().restaurant(restaurant1).numPeople(2).tableNumber(1).build());
        order2 = orderRepository.save(Order.builder().restaurant(restaurant1).numPeople(2).tableNumber(1).build());
        // platos

        // lineas pedido OrderLine
        orderLineRepository.save(OrderLine.builder().order(order2).quantity(2).dish(dish1).build());
        orderLineRepository.save(OrderLine.builder().order(order2).quantity(2).dish(dish2).build());
    }

    @Test
    void verificarValoresPorDefecto() {
        // fecha y estado pendiente
        assertNotNull(order1.getId());
        assertNotNull(order1.getDate());
        assertEquals(LocalDateTime.now().toLocalDate(), order1.getDate().toLocalDate());
        assertEquals(OrderStatus.PENDING, order1.getStatus());
    }
    @Test
    void findAllByRestaurant() {
        List<Order> pedidos = orderRepository.findByRestaurantId(restaurant1.getId());
        assertTrue(pedidos.size() >= 2);
    }
    @Test
    void calculateTotalPrice() {
        Double totalPrice = orderRepository.calculateTotalPrice(order1.getId()); // calcula el precio total del pedido
        assertEquals(0.0, totalPrice); // es 0.0 porque no tiene lineas de pedido

        totalPrice = orderRepository.calculateTotalPrice(order2.getId()); // calcula el precio total del pedido
        assertEquals(60.0, totalPrice);
    }
}