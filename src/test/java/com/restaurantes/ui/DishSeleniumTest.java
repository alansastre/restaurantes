package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DishSeleniumTest extends BaseSeleniumTest {

    @Test
    void dishList() {
        driver.get(baseUrl + "dishes");
        assertEquals("Platos", driver.findElement(By.tagName("h1")).getText());
        assertTrue(driver.findElement(By.id("resultsCount")).getText().contains("plato"));

//        WebElement dishes = driver.findElements(By.className("card"))
        List<WebElement> dishes = driver.findElements(By.cssSelector("#dishGrid .card"));
        assertTrue(dishes.size() >= 2);

        WebElement firstDish = dishes.getFirst();
        assertTrue(firstDish.getText().contains(pizza.getName()));
        assertTrue(firstDish.getText().contains(pizza.getPrice().toString().replace(".", ",")));
        assertTrue(firstDish.getText().contains("Principales"));
        assertTrue(firstDish.getText().contains(pizza.getDescription()));
        assertTrue(firstDish.getText().contains(pizza.getRestaurant().getName()));

        firstDish.findElement(By.linkText("Ver")).click();

        assertEquals(baseUrl + "dishes/" + pizza.getId(), driver.getCurrentUrl());
    }

    // dishDetail
}
