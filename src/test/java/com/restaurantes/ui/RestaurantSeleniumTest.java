package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantSeleniumTest extends BaseSeleniumTest {

    @Test
    void restaurantList () {
        driver.get(baseUrl + "restaurants");

        assertEquals("Bienvenido a la lista de restaurantes", driver.findElement(By.tagName("h1")).getText());

        assertTrue(driver.findElement(By.id("resultsCount")).getText().contains("resultado"));

        List<WebElement> restaurants = driver.findElements(By.className("card-restaurant"));
        assertFalse(restaurants.isEmpty());

        WebElement firstRestaurant = restaurants.getFirst();
        assertTrue(firstRestaurant.getText().contains(pizzeria.getName()));
        assertTrue(firstRestaurant.getText().contains(pizzeria.getAveragePrice().toString()));
        assertTrue(firstRestaurant.getText().contains("Todo tipo"));

        // Como no es admin no puede ver el botón de Editar
        assertTrue(firstRestaurant.findElements(By.linkText("Editar")).isEmpty());

        WebElement viewBtn = firstRestaurant.findElement(By.linkText("Ver"));
        viewBtn.click();

        assertEquals(baseUrl + "restaurants/" + pizzeria.getId(), driver.getCurrentUrl());
    }
}
