package com.restaurantes.ui;

import com.restaurantes.model.*;
import com.restaurantes.model.enums.DishType;
import com.restaurantes.model.enums.Role;
import com.restaurantes.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@ExtendWith(ScreenshotOnFailure.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseSeleniumTest {

    @LocalServerPort
    int port;
    @Autowired
    RestaurantRepository restaurantRepo;
    @Autowired
    DishRepository dishRepo;
    @Autowired
    ReviewRepository reviewRepo;
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    OrderRepository orderRepo;
    @Autowired
    OrderLineRepository orderLineRepo;

    String baseUrl;
    WebDriver driver;
    WebDriverWait wait;

    Restaurant pizzeria; // con platos
    Restaurant taberna; // sin platos
    Dish pizza;
    Dish tiramisu;
    Review pizzeriaOK;
    Review pizzeriaMal;
    Review pizzaOK;
    User user, admin;
    Order orderPizzas;
    Order orderConPlatos;


    @BeforeEach
    void setUp() {
        // crear datos demo
        orderLineRepo.deleteAll();
        orderRepo.deleteAll();
        reviewRepo.deleteAll();
        dishRepo.deleteAll();
        restaurantRepo.deleteAll();
        userRepo.deleteAll();

        user = userRepo.save(User.builder()
                .username("user").email("user@gmail.com").password(passwordEncoder.encode("user")).role(Role.ROLE_USER)
                .build());

        admin = userRepo.save(User.builder()
                .username("admin").email("admin@gmail.com").password(passwordEncoder.encode("admin")).role(Role.ROLE_ADMIN)
                .build());

        pizzeria = restaurantRepo.save(Restaurant.builder().name("Pizzeria Luigi").averagePrice(10.0).active(true).description("Masa artesanal").build());
        taberna = restaurantRepo.save(Restaurant.builder().name("Taberna").averagePrice(20.0).active(false).build());
        pizza = dishRepo.save(Dish.builder().name("Pizza 4 Quesos").price(12d).type(DishType.MAIN_COURSE).description("pizza bien").restaurant(pizzeria).build());
        tiramisu = dishRepo.save(Dish.builder().name("Tiramisú Café").price(3d).type(DishType.DESSERT).description("fetén").restaurant(pizzeria).build());
        pizzeriaOK = reviewRepo.save(Review.builder().title("Pectacular").rating(5).restaurant(pizzeria).content("Asombroso").build());
        pizzeriaMal = reviewRepo.save(Review.builder().title("Fatal").rating(1).restaurant(pizzeria).content("Nada bien").creationDate(LocalDateTime.now().minusDays(1)).build());
        pizzaOK = reviewRepo.save(Review.builder().title("excelente pizza").rating(5).dish(pizza).content("ok").creationDate(LocalDateTime.now().minusDays(1)).build());
        orderPizzas = orderRepo.save(Order.builder().numPeople(2).tableNumber(1).restaurant(pizzeria).user(user).build());

        orderConPlatos = orderRepo.save(Order.builder().numPeople(2).tableNumber(1).totalPrice(39d).restaurant(pizzeria).user(user).build());

        // lineas de pedido para el test de order finish
        orderLineRepo.save(OrderLine.builder().dish(pizza).order(orderConPlatos).quantity(3).build());
        orderLineRepo.save(OrderLine.builder().dish(tiramisu).order(orderConPlatos).quantity(1).build());


        // inicializar y configuración de driver
        baseUrl = "http://localhost:" + port + "/";

        // options para GitHub Actions
        boolean ci = System.getenv("CI") != null; // GitHub Actions pone CI=True
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--window-size=1920,1080");
        // Forzar es-ES en el navegador -> Accept-Language es-ES -> el servidor formatea los
        // decimales con coma igual en local y en CI. (El <input type="date"> NO se controla
        // con esto en Linux: usa el locale del SO; por eso su fecha se fija por valor ISO
        // en el propio test, no tecleando.)
        chromeOptions.addArguments("--lang=es-ES");
        chromeOptions.setExperimentalOption("prefs", Map.of("intl.accept_languages", "es-ES"));
        if (ci) {
            chromeOptions.addArguments("--headless=new", "--no-sandbox", "--disable-gpu", "--disable-dev-shm-usage");
        }
        driver = new ChromeDriver(chromeOptions);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
    }
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    void loginAdmin() {
        login("admin", "admin");
    }
    void loginUser() {
        login("user", "user");
    }
    void login(String username, String password) {
        driver.get(baseUrl + "login");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        wait.until(driver -> !driver.getCurrentUrl().contains("/login"));
    }
}
