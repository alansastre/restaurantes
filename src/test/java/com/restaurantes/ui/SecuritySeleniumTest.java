package com.restaurantes.ui;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SecuritySeleniumTest extends BaseSeleniumTest{
    @Test
    public void anonymousTryAccessProtectedPathRedirectToLogin(){
        driver.get(baseUrl + "orders");
        wait.until(ExpectedConditions.urlContains("login"));
        loginUser();
        wait.until(ExpectedConditions.urlContains("orders"));
    }

    @Test
    void userTryAccessAdminPath() {
        loginUser();
        driver.get(baseUrl + "restaurants/new");
        wait.until(driver -> driver.findElement(By.tagName("h1"))
                .getText().contains("Sin permisos para acceder a esta sección, solo VIPs."));
    }
}
