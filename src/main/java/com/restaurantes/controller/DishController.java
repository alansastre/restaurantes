package com.restaurantes.controller;

import com.restaurantes.model.Dish;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import com.restaurantes.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

// anotaciones
@Controller
@AllArgsConstructor
public class DishController {

    // inyectar repositorios
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("dishes") // CONTROLADOR
    public String dishes(Model model) {
        model.addAttribute("dishes", dishRepository.findAll());
        return "dishes/dish-list"; // VISTA
    }

    // mapping
    @GetMapping("dishes/{id}")
    public String dishDetail(Model model, @PathVariable Long id) {
        model.addAttribute("dish", dishRepository.findById(id).orElseThrow());
        model.addAttribute("reviews", reviewRepository.findByDish_IdOrderByCreationDateDesc(id));
       // alergenos
        // ingredientes
        // photos
        return "dishes/dish-detail";
    }


    @GetMapping("dishes/new")
    public String create(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "dishes/dish-form";
    }

    // TODO  @GetMapping ("dishes/edit/{id}")


    @PostMapping("dishes")
    public String save(@ModelAttribute Dish dish) {
        dishRepository.save(dish);
        return "redirect:/dishes/" + dish.getId();
    }



}
