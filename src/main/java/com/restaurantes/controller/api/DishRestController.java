package com.restaurantes.controller.api;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
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

    @PostMapping("dishes")
    public ResponseEntity<Dish> create(@RequestBody Dish dish) {
        if (dish.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish id must be null");
        }
        dish.setRestaurant(resolveRestaurant(dish));
        Dish saved = dishRepository.save(dish);
        return ResponseEntity.created(URI.create("/api/v1/dishes/" + saved.getId())).body(saved);
    }

    @PutMapping("dishes/{id}")
    public ResponseEntity<Dish> update(
            @PathVariable Long id,
            @RequestBody Dish dish)
    {
        Dish existing = dishRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish " + id + " not found")
        );
        existing.setName(dish.getName());
        existing.setPrice(dish.getPrice());
        existing.setDescription(dish.getDescription());
        existing.setType(dish.getType());
        existing.setActive(dish.getActive());
        existing.setRestaurant(resolveRestaurant(dish));
        existing.setImageUrl(dish.getImageUrl());
        // como alternativa se podría usar DTOs y mappers
        // existing.setStartDate(restaurant.getStartDate()); // conservar fecha original
        return ResponseEntity.ok(dishRepository.save(existing));
    }


    private Restaurant resolveRestaurant(Dish dish) {
        if (dish.getRestaurant() == null || dish.getRestaurant().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant id must be set");
        }
        return restaurantRepository.findById(dish.getRestaurant().getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found")
        );
    }

}
