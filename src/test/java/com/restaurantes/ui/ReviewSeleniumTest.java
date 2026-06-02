package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewSeleniumTest extends BaseSeleniumTest{

    @Test
    void reviewsList() {
        driver.navigate().to(baseUrl + "reviews");

        List<WebElement> reviews = driver.findElements(By.cssSelector("#reviewsGrid .card"));
        WebElement firstReview = reviews.getFirst();

        assertTrue(firstReview.getText().contains(pizzeriaOK.getTitle()));
        assertTrue(firstReview.getText().contains(pizzeriaOK.getContent()));
        assertTrue(firstReview.getText().contains(pizzeriaOK.getRating().toString()));

        assertTrue(driver.findElement(By.tagName("h1")).getText()
                .contains("Opiniones de clientes"));

    }

    @Test
    void reviewDetail() {
        driver.navigate().to(baseUrl + "reviews/" + pizzeriaOK.getId());

        WebElement breadcrumb = driver.findElement(By.className("breadcrumb"));
        assertTrue(breadcrumb.getText().contains("Reseña #" +  pizzeriaOK.getId()));

        WebElement review = driver.findElement(By.id("reviewInfo"));
        assertTrue(review.getText().contains(pizzeriaOK.getTitle()));
        assertTrue(review.getText().contains(pizzeriaOK.getRating().toString()));
        assertTrue(review.getText().contains(pizzeriaOK.getRestaurant().getName()));

        // Botón de volver al listado dish-list
        driver.findElement(By.linkText("Volver al listado")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "reviews"));
        assertEquals(baseUrl + "reviews", driver.getCurrentUrl());

        // Botón de editar (primero volvemos al detail)
        driver.navigate().back();
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.linkText("Editar")));
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.linkText("Borrar")));


//        driver.navigate().to(baseUrl + "reviews/" + pizzeriaOK.getId());
    }
    @Test
    void reviewDetailDeleteAdmin() {
        loginAdmin();
        driver.navigate().to(baseUrl + "reviews/" + pizzeriaOK.getId());

        driver.findElement(By.linkText("Borrar")).click();

        wait.until(ExpectedConditions.alertIsPresent()); // esperar que salga el confirm
        driver.switchTo().alert().accept(); // aceptar el confirm
//        driver.switchTo().alert().dismiss(); // cancelar el confirm

        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "reviews"));

        assertTrue(driver.findElement(By.className("alert-success"))
                .getText().contains("Borrado exitosamente"));
    }

    @Test
    void createReviewRestaurant() {
        loginUser();
        driver.navigate().to(baseUrl + "restaurants/" + pizzeria.getId());

        WebElement reviewButton = driver.findElement(By.linkText("Escribir reseña"));

        /*
        el botón "Escribir reseña" está abajo, no visible en el viewport
        por tanto hay que usar una Action para desplazarse hasta el elemento
         */
        new Actions(driver).moveToElement(reviewButton).click().perform();

        wait.until(driver -> driver.getCurrentUrl()
                .equals(baseUrl + "reviews/new?restaurantId=" + pizzeria.getId()));

        driver.findElement(By.id("title")).sendKeys("Review Test");
        driver.findElement(By.id("content")).sendKeys("Review Test contenido ok");
        driver.findElement(By.id("rating")).sendKeys("5");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(driver -> driver.getCurrentUrl()
                .equals(baseUrl + "restaurants/" + pizzeria.getId()));

        assertTrue(driver.findElement(By.tagName("main")).getText()
                .contains("Review Test contenido ok"));
    }

    @Test
    void createReviewDish(){
        loginUser();
        driver.navigate().to(baseUrl + "dishes/" + tiramisu.getId());

        WebElement reviewButton = driver.findElement(By.linkText("Escribir reseña"));

        /*
        el botón "Escribir reseña" está abajo, no visible en el viewport
        por tanto hay que usar una Action para desplazarse hasta el elemento
         */
        new Actions(driver).moveToElement(reviewButton).click().perform();

        wait.until(driver -> driver.getCurrentUrl()
                .equals(baseUrl + "reviews/new?dishId=" + tiramisu.getId()));

        driver.findElement(By.id("title")).sendKeys("Review Test");
        driver.findElement(By.id("content")).sendKeys("Review Test contenido ok");
        driver.findElement(By.id("rating")).sendKeys("5");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(driver -> driver.getCurrentUrl()
                .equals(baseUrl + "dishes/" + tiramisu.getId()));

        assertTrue(driver.findElement(By.tagName("main")).getText()
                .contains("Review Test contenido ok"));
    }

}
