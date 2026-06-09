package com.restaurantes.controller.api;

import com.restaurantes.model.Dish;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
