package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Employee;
import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantController {

//    @Autowired

    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;
    // TODO private ReviewRepository

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
        return "restaurants/restaurant-list"; // VISTA HTML
    }

    // Metodo para ver el detalle de un restaurante por id
    // /restaurants/{id}
    // findById
    @GetMapping("restaurants/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model) {

        Optional<Restaurant> restauranteOptional = restaurantRepository.findById(id);

        if (restauranteOptional.isPresent()) {
            Restaurant restaurant = restauranteOptional.get();
            model.addAttribute("restaurant", restaurant);
            List<Dish> platos = dishRepository.findByRestaurant_Id(id);
            model.addAttribute("dishes", platos);
            // TODO - traer y cargar employees
            // TODO - traer y cargar las Review
            // reviewRepository.findByRestaurantId
            // TODO - traer y cargar los Order (Pedidos)
            return "restaurants/restaurant-detail";
        }

        return "redirect:/restaurants";
        // if optional is present
            // get
    }

}

