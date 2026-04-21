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

//    @Autowired
    private RestaurantRepository restaurantRepository;
    // DishRepository
    // OrderRepository
    // EmployeeRepository

    // Usando Lombok @AllArgsConstructor no hace falta añadir manualmente el constructor, lo genera automáticamente
//    public RestaurantController(RestaurantRepository restaurantRepository) {
//        this.restaurantRepository = restaurantRepository;
//    }

    // alternativa sería crear un index.html para la home ya la lee automático
    // al entrar a localhost:8080
//    @GetMapping("/")
//    public String index() {
//        return "redirect:/restaurants";
//    }
    // PATRÓN MVC
    @GetMapping("restaurants") // CONTROLADOR
    public String restaurants(Model model) {
        // MODEL donde se cargan datos
        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurant-list"; // VISTA HTML
    }


}

