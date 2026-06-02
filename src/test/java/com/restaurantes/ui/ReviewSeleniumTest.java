package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewSeleniumTest extends BaseSeleniumTest{

    @Test
    void reviewsList() {
        driver.navigate().to(baseUrl + "reviews");

        List<WebElement> reviews = driver.findElements(By.cssSelector("#reviewGrid .card"));
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

        // TODO botones
    }

    @Test
    void createReviewRestaurant(){
        loginUser();
    }

    @Test
    void createReviewDish(){}

}
