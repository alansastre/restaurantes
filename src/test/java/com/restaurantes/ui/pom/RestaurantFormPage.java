package com.restaurantes.ui.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RestaurantFormPage extends BasePage {

    private static final By HEADING       = By.tagName("h1");
    private static final By NAME          = By.id("name");
    private static final By AVERAGE_PRICE = By.id("averagePrice");
    private static final By ACTIVE        = By.id("active");        // checkbox
    private static final By DESCRIPTION   = By.id("description");   // textarea
    private static final By DATE          = By.id("date");         // input type=date
    private static final By CITY          = By.id("city");         // select
    private static final By FOOD_TYPE     = By.id("foodType");     // select
    private static final By SUBMIT        = By.cssSelector("button[type='submit']");

    public RestaurantFormPage(WebDriver driver, WebDriverWait wait, String baseUrl) {
        super(driver, wait, baseUrl);
    }

    // === Navegación: alta y edición comparten formulario ===
    public RestaurantFormPage openNew() {
        driver.get(baseUrl + "restaurants/new");
        return this;
    }

    public RestaurantFormPage openEdit(Long id) {
        driver.get(baseUrl + "restaurants/edit/" + id);
        return this;
    }

    // H1
    public String heading() {
        return driver.findElement(HEADING).getText();
    }

    // === Rellenar campos (devuelven this -> permiten encadenar) ===
    public RestaurantFormPage typeName(String name) {
        WebElement input = driver.findElement(NAME);
        input.clear();
        input.sendKeys(name);
        return this;
    }

    public RestaurantFormPage typeAveragePrice(String price) {
        WebElement input = driver.findElement(AVERAGE_PRICE);
        input.clear();
        input.sendKeys(price);
        return this;
    }

    public RestaurantFormPage typeDescription(String text) {
        WebElement input = driver.findElement(DESCRIPTION);
        input.clear();
        input.sendKeys(text);
        return this;
    }

    public RestaurantFormPage selectCity(String value) {
        new Select(driver.findElement(CITY)).selectByValue(value);
        return this;
    }

    public RestaurantFormPage selectFoodType(String value) {
        new Select(driver.findElement(FOOD_TYPE)).selectByValue(value);
        return this;
    }

    /**
     * El <input type="date"> interpreta el TECLEO según el locale del SO
     * (en Linux/CI usa MM/dd y rompe). Por eso fijamos el valor en ISO yyyy-MM-dd
     * vía JavaScript: es independiente del SO y del navegador.
     */
    public RestaurantFormPage setDate(String isoDate) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];",
                driver.findElement(DATE), isoDate);
        return this;
    }

    // === Lectura (para tests de edición) ===
    // getDomProperty("value") = valor ACTUAL del input (la propiedad del DOM).
    // En 2026 es preferible a getAttribute("value"), que devuelve el valor inicial.
    public String nameValue() {
        return driver.findElement(NAME).getDomProperty("value");
    }

    // === Envío: el form redirige a /restaurants -> devolvemos la lista ===
    public RestaurantListPage submit() {
        driver.findElement(SUBMIT).click();
        wait.until(d -> d.getCurrentUrl().endsWith("/restaurants"));
        return new RestaurantListPage(driver, wait, baseUrl);
    }

}
