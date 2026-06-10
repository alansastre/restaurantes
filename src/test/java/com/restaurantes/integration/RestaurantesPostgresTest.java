package com.restaurantes.integration;

import com.restaurantes.model.Restaurant;
import com.restaurantes.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Levanta el contexto COMPLETO de la app contra PostgreSQL real.
 *
 * Diferencia con el @DataJpaTest: aquí NO hace falta @AutoConfigureTestDatabase, porque
 * @SpringBootTest no sustituye el datasource por uno embebido (eso solo lo hace el slice).
 */
@SpringBootTest
class RestaurantesPostgresTest extends AbstractPostgresIT {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DataSource dataSource;

    @Test
    void laAppArrancaYPersisteEnPostgres() throws Exception {
        try (Connection c = dataSource.getConnection()) {
            assertEquals("PostgreSQL", c.getMetaData().getDatabaseProductName());
        }

        Restaurant r = restaurantRepository.save(
                Restaurant.builder().name("Integracion PG").active(true).build());

        assertNotNull(r.getId());
        assertTrue(restaurantRepository.findById(r.getId()).isPresent());
    }
}
