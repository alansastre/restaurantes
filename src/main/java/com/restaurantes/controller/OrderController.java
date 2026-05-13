package com.restaurantes.controller;


import com.restaurantes.model.Order;
import com.restaurantes.repository.OrderLineRepository;
import com.restaurantes.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    @GetMapping("orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "orders/order-list"; // VISTA
    }

    // OK DETALLE DE UN PEDIDO, TODO AGREGAR PLATOS
    @GetMapping("orders/{id}")
    public String order(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderRepository.findById(id).orElseThrow());
        // asociaciones:
        model.addAttribute("orderLines", orderLineRepository.findByOrderId(id));
        return "orders/order-detail";
    }

    // 1. NAVEGAR AL FORMULARIO PARA INICIAR PEDIDO
    @GetMapping("orders/new")
    public String newOrder(Model model, @RequestParam() Long restaurantId) {
        return "orders/order-form";
    }

    // 2. RECIBIR Y GUARDAR EL PEDIDO EN BASE DE DATOS SE GENERA CLAVE PRIMARIA
    @PostMapping("orders")
    public String createOrder(@ModelAttribute Order order){
        return "redirect:/orders/" + order.getId();
    }

    // 3. AÑADIR UN PLATO AL PEDIDO
    @PostMapping("orders/{orderId}/lines")
    public String createLine(@PathVariable Long orderId,
                             @RequestParam Long dishId) {
        return "redirect:/orders/" + orderId;
    }

    // opcional mejora UX 4. QUITAR UN PLATO DEL PEDIDO
    // localhost:8080/orders/1/lines/2/delete
    @GetMapping("orders/{orderId}/lines/{lineId}/delete")
    public String deleteLine(@PathVariable Long orderId, @PathVariable Long lineId) {
        return "redirect:/orders/" + orderId;
    }

    // opcional mejora UX 5. PODER ACTUALIZAR LA CANTIDAD DE UN PLATO EN UN PEDIDO
    // localhost:8080/orders/1/lines/2?quantity=5
    @PostMapping("orders/{orderId}/lines/{lineId}")
    public String updateLine(
            @PathVariable Long orderId,
            @PathVariable Long lineId,
            @RequestParam Integer quantity) {
        return "redirect:/orders/" + orderId;
    }

    // 6. FINALIZAR PEDIDO, CAMBIA STATUS A FINISHED
    @GetMapping("orders/{id}/finish")
    public String finishOrder(@PathVariable Long id, @RequestParam Double tip){
        return "redirect:/orders/" + id;
    }

}
