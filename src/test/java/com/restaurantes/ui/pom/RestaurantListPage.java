package com.restaurantes.ui.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

// POM: Page Object Model
// https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/
public class RestaurantListPage extends BasePage {

    private static final By H1_TITLE = By.tagName("h1");
    private static final By RESULTS_COUNT = By.id("resultsCount");
    private static final By RESTAURANT_CARDS = By.className("card-restaurant");
    private static final By CREATE_BUTTON = By.linkText("Crear restaurante");

    public RestaurantListPage(WebDriver driver, WebDriverWait wait, String baseUrl) {
        super(driver, wait, baseUrl);
    }

    public RestaurantListPage open() {
        driver.get(baseUrl + "restaurants");
        return this;
    }

    public String getH1Title() {
        return driver.findElement(H1_TITLE).getText();
    }

    public String getResultsListCount() {
        return driver.findElement(RESULTS_COUNT).getText();
    }

    public List<WebElement> getRestaurantCards() {
        return driver.findElements(RESTAURANT_CARDS);
    }

    public boolean hasRestaurant(String restaurantName) {
        return getRestaurantCards().stream()
                .anyMatch(restaurantCard -> restaurantCard.getText().contains(restaurantName));
    }

    public boolean isCreateRestaurantButtonVisible() {
        return !driver.findElements(CREATE_BUTTON).isEmpty();
    }

    public RestaurantDetailPage openFirstRestaurant() {
        getRestaurantCards().getFirst().findElement(By.linkText("Ver")).click();
        return new RestaurantDetailPage(driver, wait, baseUrl);
    }

    public RestaurantFormPage clickCreateRestaurantButton() {
        driver.findElement(CREATE_BUTTON).click();
        return new RestaurantFormPage(driver, wait, baseUrl);
    }

}
