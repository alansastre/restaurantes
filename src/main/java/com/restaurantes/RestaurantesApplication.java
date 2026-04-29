package com.restaurantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantesApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(RestaurantesApplication.class, args);

//        // INTRODUCIR DATOS DE PRUEBA PARA LA INTERFAZ DE USUARIO HTML
//        RestaurantRepository restaurantRepository = context.getBean(RestaurantRepository.class);
//        DishRepository dishRepository = context.getBean(DishRepository.class);
//        ReviewRepository reviewRepository =context.getBean(ReviewRepository.class);
//
//        restaurantRepository.save(
//        Restaurant.builder().name("Restaurante 1").averagePrice(null).active(false).foodType(FoodType.SPANISH).build()
//        );
//        restaurantRepository.save(
//        Restaurant.builder().name("Restaurante 2").averagePrice(30.5).foodType(FoodType.MEXICAN).build()
//        );
//        restaurantRepository.save(
//        Restaurant.builder().name("Restaurante 3").averagePrice(40.5).foodType(FoodType.JAPANESE).build()
//        );
//        Restaurant restaurant4 = restaurantRepository.save(
//                Restaurant.builder().name("Restaurante 4").averagePrice(40.5).foodType(null).build()
//        );
//        dishRepository.save(
//                Dish.builder()
//                        .name("Dish 1")
//                        .price(10.5)
//                        .restaurant(restaurant4)
//                        .active(true)
//                        .imageUrl("https://placehold.co/100")
//                        .type(DishType.MAIN_COURSE)
//                        .build()
//        );
//
//        reviewRepository.saveAll(List.of(
//                Review.builder().title("Majestuoso").content("No tengo palabras").rating(5).restaurant(restaurant4).build(),
//                Review.builder().title("Puaj").content("Mejor no te cuento").rating(1).restaurant(restaurant4).build(),
//                Review.builder().title("OK").content("Hola soy Hilario.").rating(3).restaurant(restaurant4).build(),
//                Review.builder().title("Está bueno xd").content("LOL").rating(4).restaurant(restaurant4).build()
//        ));
    }

}
