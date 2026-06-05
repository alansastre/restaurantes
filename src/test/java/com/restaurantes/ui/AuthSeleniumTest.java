package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthSeleniumTest extends BaseSeleniumTest {

    @Test
    void login() {
        driver.get(baseUrl + "login");
        driver.findElement(By.id("username")).sendKeys(user.getUsername());
        driver.findElement(By.id("password")).sendKeys("user");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "restaurants"));
        assertEquals(baseUrl + "restaurants", driver.getCurrentUrl());
    }

    @Test
    void register() {
        driver.get(baseUrl + "register");
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.id("password")).sendKeys("testuser");
        driver.findElement(By.id("passwordConfirm")).sendKeys("testuser");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "login"));
        assertEquals(baseUrl + "login", driver.getCurrentUrl());
    }

    @Test
    void logout() {
        driver.get(baseUrl + "restaurants");

        // Primero sin autenticarse
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("loginLink"))));
        assertEquals(1, driver.findElements(By.id("loginLink")).size()); // SI LOGIN
        assertEquals(1, driver.findElements(By.id("registerLink")).size()); // SI REGISTRO
        assertTrue(driver.findElements(By.id("logoutButton")).isEmpty()); // NO LOGOUT

        loginUser();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("logoutButton"))));
        assertTrue(driver.findElements(By.id("loginLink")).isEmpty()); // NO LOGIN
        assertTrue(driver.findElements(By.id("registerLink")).isEmpty()); // NO REGISTRO
        assertEquals(user.getUsername(), driver.findElement(By.id("username")).getText()); // SI LOGOUT

        driver.findElement(By.id("logoutButton")).click();
        wait.until(ExpectedConditions.urlContains("/login?logout"));

    }

    @Test
    void registerError() {
        driver.get(baseUrl + "register");
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("email")).sendKeys("testuser@gmail.com");
        driver.findElement(By.id("password")).sendKeys("abcde");
        driver.findElement(By.id("passwordConfirm")).sendKeys("edcba");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> driver.getCurrentUrl().equals(baseUrl + "register"));

        assertTrue(driver.findElement(By.id("error")).getText()
                .contains("Las contraseñas no coinciden"));
    }
}
