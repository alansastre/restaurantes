package com.restaurantes.controller;

import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class RestaurantController {

    private RestaurantRepository restaurantRepository;

    // Usando Lombok @AllArgsConstructor no hace falta añadir manualmente el constructor, lo genera automáticamente
//    public RestaurantController(RestaurantRepository restaurantRepository) {
//        this.restaurantRepository = restaurantRepository;
//    }

    @GetMapping("restaurants")
    public String restaurants(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurant-list";
    }


}

