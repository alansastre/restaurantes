package com.restaurantes;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.model.enums.FoodType;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantesApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RestaurantesApplication.class, args);

        // INTRODUCIR DATOS DE PRUEBA PARA LA INTERFAZ DE USUARIO HTML
        RestaurantRepository restaurantRepository = context.getBean(RestaurantRepository.class);
        DishRepository dishRepository = context.getBean(DishRepository.class);

        restaurantRepository.save(
        Restaurant.builder().name("Restaurante 1").averagePrice(null).active(false).foodType(FoodType.SPANISH).build()
        );
        restaurantRepository.save(
        Restaurant.builder().name("Restaurante 2").averagePrice(30.5).foodType(FoodType.MEXICAN).build()
        );
        restaurantRepository.save(
        Restaurant.builder().name("Restaurante 3").averagePrice(40.5).foodType(FoodType.JAPANESE).build()
        );
        Restaurant restaurant4 = restaurantRepository.save(
                Restaurant.builder().name("Restaurante 4").averagePrice(40.5).foodType(null).build()
        );
        dishRepository.save(
                Dish.builder()
                        .name("Dish 1")
                        .price(10.5)
                        .restaurant(restaurant4)
                        .active(true)
                        .imageUrl("https://placehold.co/100")
                        .type(DishType.MAIN_COURSE)
                        .build()
        );
    }

}
