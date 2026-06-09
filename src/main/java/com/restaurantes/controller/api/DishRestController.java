package com.restaurantes.controller.api;

import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1")
@RestController
@AllArgsConstructor
public class DishRestController {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
}
