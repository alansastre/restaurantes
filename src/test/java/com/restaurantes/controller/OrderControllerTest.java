package com.restaurantes.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
public class OrderControllerTest {

    @Test
    @DisplayName("GET /orders")
    void list() {}
    @Test
    @DisplayName("GET /orders/{id}")
    void detail() {}
    @Test
    @DisplayName("GET /orders/new?restaurantId={id}")
    void newOrder() {}
    @Test
    @DisplayName("POST /orders")
    void createOrder() {}
    @Test
    @DisplayName("POST /orders/{orderId}/lines?dishId={id}")
    void createLine() {}
    @Test
    @DisplayName("GET /orders/{orderId}/lines/{lineId}/delete")
    void deleteLine() {}
    @Test
    @DisplayName("POST /orders/{orderId}/lines/{lineId}?quantity=2")
    void updateLine() {}
    @Test
    @DisplayName("GET /orders/{orderId}/finish?tip=0")
    void finishOrder() {}
}
