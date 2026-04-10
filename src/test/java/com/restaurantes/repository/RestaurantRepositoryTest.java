package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RestaurantRepositoryTest {


    @Autowired // Mecanismo de inyección de dependencias de Spring para obtener una instancia del repositorio
    RestaurantRepository restaurantRepository;

    @Test
    void guardarRestaurante() {
        // INSERT INTO restaurantes
         Restaurant restaurant = new Restaurant();
        // RestaurantRepository repo = new RestaurantRepository(); // No se puede porque es una interface no una class

//        Restaurant restaurant = Restaurant.builder().build();
        restaurant.setName("Restaurante");
        restaurant.setAveragePrice(20.0);
        restaurant = restaurantRepository.save(restaurant);

        assertNotNull(restaurant.getId());
        assertEquals("Restaurante", restaurant.getName());
    }
    @Test
    void buscarRestaurantes_Vacio() {
        // Probar a hacer una consulta y traer todos los restaurantes de la base de datos
        // SELECT * FROM restaurantes
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertNotNull(restaurants);
        System.out.println(restaurants);

        assertTrue(restaurants.size() == 0); // size() equivalente a .length de los arrays []
        assertTrue(restaurants.isEmpty());
    }

    @Test
    void buscarRestaurantes_ConDatos() {
        // guardar uno o dos o tres restaurantes
        // save de uno en uno
        Restaurant restaurante1 = new Restaurant("Restaurante 1", 20.6); // constructor
        Restaurant restaurante2 = Restaurant.builder().averagePrice(25.3).name("Restaurante 2").active(false).build();
        //restaurantRepository.save(restaurante2);

        // saveAll
        List<Restaurant> restaurantes = List.of(restaurante1, restaurante2); // Lista de dos restaurantes
        restaurantRepository.saveAll(restaurantes);

        List<Restaurant> restaurantsFromDatabase = restaurantRepository.findAll();
        //System.out.println(restaurantsFromDatabase);
        for (Restaurant restaurant : restaurantsFromDatabase) {
            System.out.println(restaurant);
        }

        // assert comprobar que sí hay restaurantes
        assertNotNull(restaurantsFromDatabase);
        assertFalse(restaurantsFromDatabase.isEmpty());
        assertEquals(2, restaurantsFromDatabase.size());

        // array[0]
        restaurantsFromDatabase.get(0);
        restaurantsFromDatabase.getFirst(); // nuevo desde java 21

        assertEquals("Restaurante 1", restaurantsFromDatabase.get(0).getName()); // primer elemento
        assertEquals("Restaurante 1", restaurantsFromDatabase.getFirst().getName()); // primer elemento
        assertEquals("Restaurante 2", restaurantsFromDatabase.get(1).getName()); // segundo elemento
    }


    @Test
    void buscarRestaurantePorId() {
        // Probar a hacer una consulta y traer un restaurante por su id (WHERE id = X)
    }


    // saveAll con builder
    // update
    // deleteById
    // deleteAll

}