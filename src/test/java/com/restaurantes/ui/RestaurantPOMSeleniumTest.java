package com.restaurantes.ui;

import com.restaurantes.ui.pom.RestaurantDetailPage;
import com.restaurantes.ui.pom.RestaurantListPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantPOMSeleniumTest extends BaseSeleniumTest {

    @Test
    void listadoTest() {
        RestaurantListPage list = new RestaurantListPage(driver, wait, baseUrl).open();

        assertEquals("Bienvenido a la lista de restaurantes", list.getH1Title());
        assertTrue(list.getResultsListCount().contains("resultado"));
        assertTrue(list.hasRestaurant(pizzeria.getName()));
        assertFalse(list.isCreateRestaurantButtonVisible());

        RestaurantDetailPage detail = list.openFirstRestaurant();

    }
}
