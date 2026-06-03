package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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

        // agregar plato pizza
//        List<WebElement> dishButtons = driver.findElements(By.cssSelector("#dishGrid .btn-success"));
//        WebElement pizzaButton = dishButtons.getFirst();

        WebElement pizzaButton = driver.findElement(By.id("addDish-" + pizza.getId()));
        new Actions(driver).moveToElement(pizzaButton).click().perform();// pizza 12 €
        assertTrue(driver.findElement(By.id("orderTotalPrice")).getText().contains("12,00"));

        // agregar plato tiramisu
        WebElement tiramisuButton = driver.findElement(By.id("addDish-" + tiramisu.getId()));
        new Actions(driver).moveToElement(tiramisuButton).click().perform();
        assertTrue(driver.findElement(By.id("orderTotalPrice")).getText().contains("15,00"));

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
