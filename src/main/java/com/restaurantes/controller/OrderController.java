package com.restaurantes.controller;


import com.restaurantes.repository.OrderLineRepository;
import com.restaurantes.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    @GetMapping("orders") // CONTROLADOR
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "orders/order-list"; // VISTA
    }

    @GetMapping("orders/{id}")
    public String order(Model model, @PathVariable Long id) {
        model.addAttribute("order", orderRepository.findById(id).orElseThrow());
        // asociaciones:
        model.addAttribute("orderLines", orderLineRepository.findByOrderId(id));
        return "orders/order-detail";
    }
}
