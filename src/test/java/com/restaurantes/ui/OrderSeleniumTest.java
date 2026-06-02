package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

public class OrderSeleniumTest extends BaseSeleniumTest {

    @Test
    void startOrder() {
        loginUser();
        driver.navigate().to(baseUrl + "restaurants/" + pizzeria.getId());
        driver.findElement(By.linkText("Hacer pedido")).click();
        wait.until(ExpectedConditions.urlContains("/orders/new?restaurantId=" + pizzeria.getId()));
        driver.findElement(By.id("tableNumber")).sendKeys("1");
        driver.findElement(By.id("numPeople")).sendKeys("2");
        driver.findElement(By.id("userSuggestions")).sendKeys("al fondo a la derecha");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(ExpectedConditions.urlContains("/orders/"));
        assertTrue(driver.findElement(By.tagName("h1")).getText().contains("Pedido #"));
    }

    @Test
    void updateOrder() {
        loginUser();
        driver.navigate().to(baseUrl + "orders/" + orderPizzas.getId());
        // agregar plato
        // agregar otro plato
        // aumentar candidad plato
        // aumentar cantidad otro plato
        // decrementar cantidad plato
        // quitar plato
    }

    @Test
    void finishOrder() {
        // pagar y finalizar pedido y comprobar que cambia de estado y no deja añadir más platos
    }
}
