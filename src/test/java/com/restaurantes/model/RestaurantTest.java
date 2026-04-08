package com.restaurantes.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase para probar métodos de Restaurant
 * constructor
 * getter
 * setter
 * tostring
 */
class RestaurantTest {


    @Test
    @DisplayName("Crear restaurante con constructor vacío")
    void restaurantEmptyConstructor() {
        //Restaurant restaurantePrueba = null;
        Restaurant restaurantePrueba = new Restaurant();
        assertNotNull(restaurantePrueba);
    }

    @Test
    void getName() {
            Restaurant restaurante = new Restaurant("100montaditos");
            assertEquals("100montaditos", restaurante.getName());

            Restaurant res2 = new Restaurant("prueba", 10.0);
            assertEquals("prueba", res2.getName());
            assertEquals(10.0, res2.getAveragePrice());
    }

    @Test
    void setNameTest(){
        Restaurant restaurante = new Restaurant();
        restaurante.setName("Pacos Bar");
        assertNotNull(restaurante.getName());
        assertEquals("Pacos Bar", restaurante.getName());
    }

    @Test
    void checkActiveTrue() {
        Restaurant restaurant = new Restaurant();
        assertTrue(restaurant.getActive()); // CUIDADO es Boolean con mayúscula
    }

    @Test
    void toStringTest() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pacos Bar");
        System.out.println(restaurant);
    }

    @Test
    void emptyConstructorTest() {
        Restaurant restaurant = new Restaurant();
    }

    @Test
    void allArgsConstructorTest() {
        Restaurant r = new Restaurant(1L, "paco", 4.1, true);
    }

    @Test
    void builderTest() {
        // Builder es un patrón de diseño que ayuda a construir objetos con la información exacta que queramos
        // Empieza con builder()
        // atributos que quieras
        // build()

        Restaurant restaurante = Restaurant.builder().name("paco").averagePrice(9.99).build();

        assertEquals("paco", restaurante.getName());
        assertEquals(9.99, restaurante.getAveragePrice());
    }




    //    @Test
//    void saveRestaurant() {
//
//        // INSERT INTO restaurantes
//        // RestaurantRepository restaurantRepository = ...
//        restaurantRepository.save(new Restaurant())
//    }

//    @Test
//    void restaurantIsActiveTrueTest(){
//        Restaurant restaurantePrueba = new Restaurant();
//        assertTrue(restaurantePrueba.averagePrice >= 0);
//    }


}