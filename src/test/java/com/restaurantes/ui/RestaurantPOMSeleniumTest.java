package com.restaurantes.ui;

import com.restaurantes.model.Restaurant;
import com.restaurantes.ui.pom.RestaurantDetailPage;
import com.restaurantes.ui.pom.RestaurantFormPage;
import com.restaurantes.ui.pom.RestaurantListPage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
Testing que utiliza el patrón Page Object Model, utiliza Pages en lugar de hacer aquí los selectores y búsqueda
de web elements.
 */
public class RestaurantPOMSeleniumTest extends BaseSeleniumTest {

    @Test
    void listYdetailTest() {
        RestaurantListPage list = new RestaurantListPage(driver, wait, baseUrl).open();
        assertEquals("Bienvenido a la lista de restaurantes", list.getH1Title());
        assertTrue(list.getResultsListCount().contains("resultado"));
        assertTrue(list.hasRestaurant(pizzeria.getName()));
        assertFalse(list.isCreateRestaurantButtonVisible());


        RestaurantDetailPage detail = list.openFirstRestaurant();
        assertEquals("Restaurante " + pizzeria.getId(), detail.getH1Title());
        assertEquals(pizzeria.getName(), detail.getRestaurantName());
        assertTrue(detail.isActiveTrue());
        assertTrue(detail.hasDish(pizza.getName()));
    }

    @Test
    void controlDeAccesoDelBotonHacerPedido() {
        RestaurantDetailPage detail = new RestaurantDetailPage(driver, wait, baseUrl)
                .open(pizzeria.getId());
        assertFalse(detail.canStartOrder()); // sin login no aparece

        loginUser();                        // helper de BaseSeleniumTest
        detail.open(pizzeria.getId());
        assertTrue(detail.canStartOrder());  // con login sí
    }


    @Test
    void adminCreaRestaurante() {
        loginAdmin();
        RestaurantListPage list = new RestaurantListPage(driver, wait, baseUrl).open();

        RestaurantListPage afterSave = list.clickCreateRestaurantButton()       // -> RestaurantFormPage
                .typeName("Restaurante POM")
                .typeAveragePrice("20")
                .typeDescription("creado desde el test con POM")
                .selectCity("Madrid")
                .selectFoodType("SPANISH")
                .setDate("2027-06-02")
                .submit();                                       // -> RestaurantListPage

        assertTrue(afterSave.hasRestaurant("Restaurante POM"));
        assertTrue(restaurantRepo.findAll().stream()
                .anyMatch(r -> "Restaurante POM".equals(r.getName())));
    }

    @Test
    void adminEditaRestaurante() {
        loginAdmin();
        RestaurantFormPage form = new RestaurantFormPage(driver, wait, baseUrl)
                .openEdit(pizzeria.getId());

        assertEquals("Pizzeria Luigi", form.nameValue()); // viene relleno

        form.typeName("Pizzeria Editada")
                .setDate("2027-06-02")
                .submit();

        Restaurant editado = restaurantRepo.findById(pizzeria.getId()).orElseThrow();
        assertEquals("Pizzeria Editada", editado.getName());
    }
}
