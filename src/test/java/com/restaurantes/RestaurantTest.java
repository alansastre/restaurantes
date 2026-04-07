package com.restaurantes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

//    @Test
//    void restaurantIsActiveTrueTest(){
//        Restaurant restaurantePrueba = new Restaurant();
//        assertTrue(restaurantePrueba.averagePrice >= 0);
//    }


}