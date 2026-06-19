package com.restaurantes.ui.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class RestaurantDetailPage extends BasePage {

    private static final By H1_TITLE = By.tagName("h1");
    private static final By RESTAURANT_NAME = By.id("restaurantName");
    private static final By ACTIVE_TRUE = By.id("activeTrue");
    private static final By DISH_ROWS = By.cssSelector("#dishesTable tbody tr");
    private static final By ORDER_BUTTON = By.linkText("Hacer pedido");
    private static final By EDIT_BUTTON = By.linkText("Editar");


    public RestaurantDetailPage(WebDriver driver, WebDriverWait wait, String baseUrl) {
        super(driver, wait, baseUrl);
    }

    public RestaurantDetailPage open(Long restaurantId) {
        driver.get(baseUrl + "restaurants/" + restaurantId);
        return this;
    }

    public String getH1Title() {
        return driver.findElement(H1_TITLE).getText();
    }

    public String getRestaurantName() {
        return driver.findElement(RESTAURANT_NAME).getText();
    }

    public boolean isActiveTrue() {
        return driver.findElement(ACTIVE_TRUE).getText().contains("Abierto");
    }

    public List<WebElement> getDishRows() {
        return driver.findElements(DISH_ROWS);
    }

    public boolean hasDish(String dishName) {
        return getDishRows().stream()
                .anyMatch(dishRow -> dishRow.getText().contains(dishName));
    }

    public List<String> getDishes() {
        return driver.findElements(DISH_ROWS).stream().map(WebElement::getText).toList();
    }
    public boolean canStartOrder() {
        return !driver.findElements(ORDER_BUTTON).isEmpty();
    }

    public RestaurantFormPage clickEditButton() {
        driver.findElement(EDIT_BUTTON).click();
        return new  RestaurantFormPage(driver, wait, baseUrl);
    }

}
