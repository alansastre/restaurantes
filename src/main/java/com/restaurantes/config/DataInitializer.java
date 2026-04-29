package com.restaurantes.config;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.model.enums.FoodType;
import com.restaurantes.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private DishRepository dishRepo;
    private RestaurantRepository restaurantRepo;
    private OrderRepository orderRepo;
    private OrderLineRepository orderLineRepo;
    private ReviewRepository reviewRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("HOLA DESDE DATA INITIALIZER");

        if (restaurantRepo.count() > 0) return;

        var res1 = restaurantRepo.save(Restaurant.builder().name("R1").foodType(FoodType.SPANISH).averagePrice(12.50).build());
        var res2 = restaurantRepo.save(Restaurant.builder().name("R2").foodType(FoodType.JAPANESE).averagePrice(15.50).build());
        var res3 = restaurantRepo.save(Restaurant.builder().name("R3").foodType(FoodType.MEXICAN).averagePrice(19.50).build());

        var dish1 = dishRepo.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato1").price(9.99).restaurant(res1).build());
        var dish2 = dishRepo.save(Dish.builder().type(DishType.STARTER).name("Plato2").price(19.99).restaurant(res1).build());
        var dish3 = dishRepo.save(Dish.builder().type(DishType.DESSERT).name("Plato3").price(29.99).restaurant(res1).build());
        var dish4 = dishRepo.save(Dish.builder().type(DishType.STARTER).name("Plato4").price(39.99).restaurant(res2).build());
        var dish5 = dishRepo.save(Dish.builder().type(DishType.DESSERT).name("Plato5").price(49.99).restaurant(res2).build());
        var dish6 = dishRepo.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato6").price(9.99).restaurant(res3).build());



    }
}
