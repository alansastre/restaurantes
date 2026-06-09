package com.restaurantes.ui;

import com.restaurantes.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Test
    void restaurantDetail () {
        driver.get(baseUrl + "restaurants/" +  pizzeria.getId());

        // info restaurante
        assertEquals("Restaurante " + pizzeria.getId(), driver.findElement(By.tagName("h1")).getText());
        assertEquals(pizzeria.getName(), driver.findElement(By.id("restaurantName")).getText());
        assertTrue(driver.findElement(By.id("activeTrue")).getText().contains("Abierto"));

        // platos
        List<WebElement> dishes = driver.findElements(By.cssSelector("#dishesTable tbody tr"));
        assertTrue(dishes.size() >= 2);
        assertTrue(dishes.getFirst().getText().contains(pizza.getName()));
        assertTrue(dishes.get(1).getText().contains(tiramisu.getName()));

        // reviews
        List<WebElement> reviews = driver.findElements(By.cssSelector("#reviewsGrid .card"));
        assertTrue(reviews.size() >= 2);
        WebElement firstReview = reviews.getFirst();
        assertEquals(pizzeriaOK.getTitle(), firstReview.findElement(By.tagName("h5")).getText());
        assertEquals(pizzeriaOK.getTitle(), firstReview.findElement(By.cssSelector(".card-title")).getText());
        WebElement secondReview = reviews.get(1);
        assertEquals(pizzeriaMal.getTitle(), secondReview.findElement(By.tagName("h5")).getText());
        assertEquals(pizzeriaMal.getContent(), secondReview.findElement(By.cssSelector(".card-text")).getText());
        assertEquals("1/5", secondReview.findElement(By.className("review-rating")).getText());
    }

    // restaurant list filters

    @Test
    void restaurantForm(){
        loginAdmin();
        driver.get(baseUrl + "restaurants/new");
        driver.findElement(By.id("name")).sendKeys("restaurantest");
        driver.findElement(By.id("averagePrice")).sendKeys("20");
        // driver.findElement(By.id("active")).click(); // ya viene marcado por defecto
        driver.findElement(By.id("description")).sendKeys("descripcion de restaurante");
        // El <input type="date"> guarda/envia el valor en ISO yyyy-MM-dd en cualquier SO;
        // lo fijamos por valor para no depender de como Chrome interpreta el tecleo
        // (Linux usa el locale del SO -> MM/dd y rompe la fecha en CI).
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '2027-06-02';", driver.findElement(By.id("date")));
//        driver.findElement(By.id("city")).sendKeys("Madrid");

        Select foodTypeSelector = new Select(driver.findElement(By.id("foodType")));
        foodTypeSelector.selectByValue("SPANISH");
        new Select(driver.findElement(By.id("city"))).selectByValue("Madrid");

        //wait.until(driver -> driver.findElement(By.cssSelector("button[type='submit']")).isDisplayed());
        new Actions(driver).moveToElement(
                driver.findElement(By.cssSelector("button[type='submit']"))
        ).click().perform();

        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "restaurants"));
        assertEquals(baseUrl + "restaurants", driver.getCurrentUrl());

        List<WebElement> restaurants = driver.findElements(By.className("card-restaurant"));

        assertTrue(restaurants.stream()
                .anyMatch(restaurant -> restaurant.getText().contains("restaurantest")));

        assertTrue(restaurants.getLast().getText().contains("restaurantest"));

        Restaurant saved = restaurantRepo.findAll().getLast();
        assertEquals("restaurantest", saved.getName());
        assertEquals(20d, saved.getAveragePrice());
    }

    @Test
    void editRestaurant() {
        loginAdmin();
        driver.get(baseUrl + "restaurants/edit/" + pizzeria.getId());

        // verificar que los inputs están rellenos
        WebElement nameInput = driver.findElement(By.id("name"));
        assertEquals("Pizzeria Luigi", nameInput.getAttribute("value"));

        WebElement priceInput = driver.findElement(By.id("averagePrice"));
        assertEquals("10.0", priceInput.getAttribute("value"));


        nameInput.clear();
        nameInput.sendKeys("Pizzeria Editada");

        // Fijar la fecha por su valor ISO (independiente del locale del SO/navegador).
        WebElement dateInput = driver.findElement(By.id("date"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '2027-06-02';", dateInput);


        new Actions(driver).moveToElement(
                driver.findElement(By.cssSelector("button[type='submit']"))
        ).click().perform();

        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "restaurants"));

        restaurantRepo.findById(pizzeria.getId()).ifPresent(restaurant -> {
            assertEquals("Pizzeria Editada", restaurant.getName());
            assertEquals("2027-06-02", restaurant.getDate().format(DateTimeFormatter.ISO_DATE));
        });
    }
}
