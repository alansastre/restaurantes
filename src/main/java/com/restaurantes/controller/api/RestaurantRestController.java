package com.restaurantes.controller.api;

import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
// http://localhost:8080/swagger-ui/index.html
@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantRestController {

    private final RestaurantRepository restaurantRepository;

    @GetMapping
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @GetMapping("{id}")
    public Restaurant findOne(@PathVariable Long id) {
        return restaurantRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant " + id + " not found")
        );
    }

    @PostMapping // solo crear
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        if (restaurant.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Restaurant ID must be null");
        }
        Restaurant saved = restaurantRepository.save(restaurant);
        //        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        return ResponseEntity.created(URI.create("/api/v1/restaurants/" + saved.getId())).body(saved);
    }

    // update

    // update partial

    // delete
}
