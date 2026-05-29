package com.restaurantes.ui;

import com.restaurantes.model.Dish;
import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.DishRepository;
import com.restaurantes.repository.RestaurantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSeleniumTest {

    @LocalServerPort
    int port;
    @Autowired
    RestaurantRepository restaurantRepo;
    @Autowired
    DishRepository dishRepo;

    String baseUrl;
    WebDriver driver;

    Restaurant pizzeria; // con platos
    Restaurant taberna; // sin platos
    Dish pizza;

    @BeforeEach
    void setUp() {
        // crear datos demo
        restaurantRepo.deleteAll();
        pizzeria = restaurantRepo.save(Restaurant.builder().name("Pizzeria Luigi").averagePrice(10.0).active(true).build());
        taberna = restaurantRepo.save(Restaurant.builder().name("Taberna").averagePrice(20.0).active(false).build());

        // inicializar y configuración de driver
        baseUrl = "http://localhost:" + port + "/";
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
