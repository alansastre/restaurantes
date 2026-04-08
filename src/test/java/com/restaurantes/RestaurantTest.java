package com.restaurantes;

import org.junit.jupiter.api.BeforeEach;
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