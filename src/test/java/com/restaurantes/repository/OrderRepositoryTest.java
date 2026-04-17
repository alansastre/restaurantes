package com.restaurantes.repository;

import com.restaurantes.model.Order;
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

    // declarar datos para las pruebas
    Restaurant restaurant1;
    Order order1;

    @BeforeEach
    void setUp() {
        // inicializar datos de prueba
        restaurant1 = restaurantRepository.save(Restaurant.builder().name("Restaurante 1").build());
        // crear y guardar un pedido en base de datos
        order1 = orderRepository.save(Order.builder().restaurant(restaurant1).numPeople(2).tableNumber(1).build());
    }

    @Test
    void verificarValoresPorDefecto() {
        // fecha y estado pendiente
        assertNotNull(order1.getId());
        assertNotNull(order1.getDate());
        assertEquals(LocalDateTime.now().toLocalDate(), order1.getDate().toLocalDate());
        assertEquals(OrderStatus.PENDIENTE, order1.getStatus());
    }
    @Test
    void findAllByRestaurant() {
        List<Order> pedidos = orderRepository.findByRestaurantId(restaurant1.getId());
        assertEquals(1, pedidos.size());
    }
}