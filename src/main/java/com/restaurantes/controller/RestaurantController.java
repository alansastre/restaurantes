package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Employee;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.Review;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantController {

    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;
    private ReviewRepository reviewRepository;

    @GetMapping("restaurants") // CONTROLADOR
    public String restaurants(
            Model model,
            @RequestParam(required = false) Double price
    ) {
        model.addAttribute("restaurants", restaurantRepository.findActiveFiltering(price));
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurants/restaurant-list"; // VISTA HTML
    }

    @GetMapping("restaurants/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model) {
        Optional<Restaurant> restauranteOptional = restaurantRepository.findByIdAndActiveTrue(id);
        if (restauranteOptional.isPresent()) {
            Restaurant restaurant = restauranteOptional.get();
            model.addAttribute("restaurant", restaurant);
            List<Dish> platos = dishRepository.findByRestaurant_Id(id);
            model.addAttribute("dishes", platos);
            List<Review> reviews = reviewRepository.findByRestaurant_IdOrderByCreationDateDesc(id);
            model.addAttribute("reviews", reviews);
            return "restaurants/restaurant-detail";
        }

        return "redirect:/restaurants";
        // if optional is present
            // get
    }


    @GetMapping("restaurants/deactivate/{id}")
    public String deactivateRestaurant(@PathVariable Long id, Model model) {

        // forma 1:
//        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
//        if (restaurantOptional.isPresent()) {
//            Restaurant restaurant = restaurantOptional.get();
//            restaurant.setActive(false);
//            restaurantRepository.save(restaurant);
//        }
//        return "redirect:/restaurants";

        // forma 2 (opcional):
        restaurantRepository.findById(id).ifPresent(restaurant -> {
            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
        });
        return "redirect:/restaurants";
    }
}

