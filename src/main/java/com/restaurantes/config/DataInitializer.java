package com.restaurantes.config;

import com.restaurantes.model.*;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.model.enums.FoodType;
import com.restaurantes.model.enums.OrderStatus;
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

        // if (restaurantRepo.count() > 0) return;

        // restaurantes:
        var res1 = restaurantRepo.save(Restaurant.builder().name("Burguer King").foodType(FoodType.SPANISH).averagePrice(12.50).build());
        var res2 = restaurantRepo.save(Restaurant.builder().name("Carlos Pizza").foodType(FoodType.JAPANESE).averagePrice(15.50).build());
        var res3 = restaurantRepo.save(Restaurant.builder().name("Sushi Lovers").foodType(FoodType.MEXICAN).averagePrice(19.50).build());
        var res4 = restaurantRepo.save(Restaurant.builder().name("Smash Burguer").foodType(FoodType.SPANISH).averagePrice(60d).build());

        // platos
        var dish1 = dishRepo.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato1").price(9.99).restaurant(res1).build());
        var dish2 = dishRepo.save(Dish.builder().type(DishType.STARTER).name("Plato2").price(19.99).restaurant(res1).build());
        var dish3 = dishRepo.save(Dish.builder().type(DishType.DESSERT).name("Plato3").price(29.99).restaurant(res1).build());
        var dish4 = dishRepo.save(Dish.builder().type(DishType.STARTER).name("Plato4").price(39.99).restaurant(res2).build());
        var dish5 = dishRepo.save(Dish.builder().type(DishType.DESSERT).name("Plato5").price(49.99).restaurant(res2).build());
        var dish6 = dishRepo.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato6").price(9.99).restaurant(res3).build());

        // reviews
        var review1 = reviewRepo.save(Review.builder().title("OK").rating(5).restaurant(res1).build());
        var review2 = reviewRepo.save(Review.builder().title("Bad").rating(1).restaurant(res1).build());
        var review3 = reviewRepo.save(Review.builder().title("Neutral").rating(3).restaurant(res1).build());

        var review4 = reviewRepo.save(Review.builder().title("OK").rating(5).dish(dish1).build());
        var review5 = reviewRepo.save(Review.builder().title("Bad").rating(1).dish(dish1).build());
        var review6 = reviewRepo.save(Review.builder().title("Neutral").rating(3).dish(dish1).build());

        // pedidos y lineas pedido
        var order1 = orderRepo.save(Order.builder().numPeople(2).tableNumber(1).totalPrice(30d).status(OrderStatus.PENDING).restaurant(res1).build());
        var order2 = orderRepo.save(Order.builder().numPeople(4).tableNumber(2).totalPrice(60d).status(OrderStatus.FINISHED).restaurant(res1).build());
        var order3 = orderRepo.save(Order.builder().numPeople(6).tableNumber(3).totalPrice(70d).status(OrderStatus.PENDING).restaurant(res1).build());

        var line1 = orderLineRepo.save(OrderLine.builder().quantity(2).dish(dish1).order(order1).build());
        var line2 = orderLineRepo.save(OrderLine.builder().quantity(1).dish(dish2).order(order1).build());
        var line3 = orderLineRepo.save(OrderLine.builder().quantity(3).dish(dish3).order(order1).build());
        var line4 = orderLineRepo.save(OrderLine.builder().quantity(3).dish(dish4).order(order2).build());
        var line5 = orderLineRepo.save(OrderLine.builder().quantity(3).dish(dish5).order(order2).build());
        var line6 = orderLineRepo.save(OrderLine.builder().quantity(3).dish(dish6).order(order2).build());

    }
}
