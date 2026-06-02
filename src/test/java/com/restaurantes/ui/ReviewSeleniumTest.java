package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
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
    void createReviewRestaurant(){
        loginUser();
    }

    @Test
    void createReviewDish(){}

}
