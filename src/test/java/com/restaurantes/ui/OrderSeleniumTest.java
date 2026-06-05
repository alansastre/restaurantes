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
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Hacer pedido")));
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
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("orderTotalPrice"), "12,00"));
        assertTrue(driver.findElement(By.id("orderTotalPrice")).getText().contains("12,00"));

        // agregar plato tiramisu
        WebElement tiramisuButton = driver.findElement(By.id("addDish-" + tiramisu.getId()));
        new Actions(driver).moveToElement(tiramisuButton).click().perform();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("orderTotalPrice"), "15,00"));
        assertTrue(driver.findElement(By.id("orderTotalPrice")).getText().contains("15,00"));


        // AUMENTAR LA CANTIDAD DE UN PLATO
        String quantityInputSelector = "#orderLine-" + pizza.getId() + " input[name='quantity']";
        WebElement pizzaQuantityInput = driver.findElement(By.cssSelector(quantityInputSelector));
        pizzaQuantityInput.clear();
        pizzaQuantityInput.sendKeys("3");
        String quantityButtonSelector =  "#orderLine-" + pizza.getId() + " .btn-success";
        WebElement pizzaQuantityButton = driver.findElement(By.cssSelector(quantityButtonSelector));
        new Actions(driver).moveToElement(pizzaQuantityButton).click().perform();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("orderTotalPrice"), "39,00"));

        // quitar plato
        WebElement pizzaRemoveButton = driver.findElement(
                By.cssSelector("#orderLine-" + pizza.getId() + " .btn-danger")
        );
        new Actions(driver).moveToElement(pizzaRemoveButton).click().perform();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("orderTotalPrice"), "3,00"));
    }

    @Test
    void finishOrder() {
        // pagar y finalizar pedido y comprobar que cambia de estado y no deja añadir más platos
            loginUser();
            driver.navigate().to(baseUrl + "orders/" + orderConPlatos.getId());

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.id("orderTotalPrice"), "39,00"));

            driver.findElement(By.id("cardOwner")).sendKeys("TITULAR TARJETA");
            driver.findElement(By.id("cardNumber")).sendKeys("1111222233334444");
            driver.findElement(By.id("cardExpirationDate")).sendKeys("03/30");
            driver.findElement(By.id("cardSecretCode")).sendKeys("777");

        new Actions(driver).moveToElement(
                driver.findElement(By.cssSelector("#paymentForm button[type='submit']"))
        ).click().perform();

            wait.until(ExpectedConditions.textToBe(By.id("orderStatus"), "Finalizado"));

            // extra: comprobar que ya no deja pagar ni agregar platos:
            assertTrue(driver.findElements(By.id("addDish-" + pizza.getId())).isEmpty());
            assertTrue(driver.findElements(By.id("cardNumber")).isEmpty());
            assertTrue(driver.findElements(
                    By.cssSelector("#orderLine-" + pizza.getId() + " input[name='quantity']"))
                    .isEmpty());

    }
}
