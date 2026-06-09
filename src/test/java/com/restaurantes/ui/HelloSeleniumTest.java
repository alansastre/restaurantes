package com.restaurantes.ui;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;
@Disabled("Intro del primer dia; redundante con RestaurantSeleniumTest.restaurantList. No extiende BaseSeleniumTest, por lo que no tiene Chrome headless y fallaria en CI.")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloSeleniumTest {

    @LocalServerPort
    int port; // puerto aleatorio

    @Test
    void restaurantList() {
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:" + port + "/restaurants");
        driver.manage().window().maximize();

        String tituloH1 = driver.findElement(By.tagName("h1")).getText();
        assertTrue(tituloH1.contains("Bienvenido a la lista de restaurantes"));
        driver.quit();
    }
}
