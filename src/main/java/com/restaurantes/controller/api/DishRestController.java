package com.restaurantes.controller.api;

import com.restaurantes.model.Dish;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("api/v1")
@RestController
@AllArgsConstructor
public class DishRestController {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;


    @GetMapping("dishes")
    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    @GetMapping("dishes/{id}")
    public Dish findById(@PathVariable Long id) {
        return dishRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish " + id + " not found")
        );
    }

    @GetMapping("restaurants/{id}/dishes")
    public List<Dish> findByRestaurant(@PathVariable Long id) {
        return dishRepository.findByRestaurant_Id(id);
    }

}
