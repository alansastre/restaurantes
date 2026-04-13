package com.restaurantes.repository;

import com.restaurantes.model.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


/*

Métodos habituales comunes en repositorios de Spring Data JPA:

 * findAll: buscar todos
 * findById: buscar uno por id
 * save: guardar uno
 * saveAll: guardar varios una lista
 * deleteById: borrar uno por id
 * delete: borrar uno por objeto entidad
 * deleteAll: borra todos, genera una sentencia delete por cada uno
 * deleteAllInBatch: borra todos, genera una sola sentencia delete sin cargar las entidades en memoria
 * existsById: devuelve boolean si existe un registro con ese id
 * count()

 HTML (navegador) --> Controller --> Repository

 */
@DataJpaTest
@DisplayName("Restaurante Repositorio operaciones de base de datos")
class RestaurantRepositoryTest {


    @Autowired // Mecanismo de inyección de dependencias de Spring para obtener una instancia del repositorio
    RestaurantRepository restaurantRepository;

    @Test
    void guardarRestaurante() {
        // INSERT INTO restaurantes
         Restaurant restaurant = new Restaurant();
        // RestaurantRepository repo = new RestaurantRepository(); // No se puede porque es una interface no una class

//        Restaurant restaurant = Restaurant.builder().build();
        restaurant.setName("Restaurante");
        restaurant.setAveragePrice(20.0);
        restaurant = restaurantRepository.save(restaurant);

        assertNotNull(restaurant.getId());
        assertEquals("Restaurante", restaurant.getName());
    }

    @Test
    void buscarRestaurantes_Vacio() {
        // Probar a hacer una consulta y traer todos los restaurantes de la base de datos
        // SELECT * FROM restaurantes
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertNotNull(restaurants);
        System.out.println(restaurants);

        assertTrue(restaurants.size() == 0); // size() equivalente a .length de los arrays []
        assertTrue(restaurants.isEmpty());
    }

    @Test
    void buscarRestaurantes_ConDatos() {
        // guardar uno o dos o tres restaurantes
        // save de uno en uno
        Restaurant restaurante1 = new Restaurant("Restaurante 1", 20.6); // constructor
        Restaurant restaurante2 = Restaurant.builder().averagePrice(25.3).name("Restaurante 2").active(false).build();
        //restaurantRepository.save(restaurante2);

        // saveAll
        List<Restaurant> restaurantes = List.of(restaurante1, restaurante2); // Lista de dos restaurantes
        restaurantRepository.saveAll(restaurantes);

        List<Restaurant> restaurantsFromDatabase = restaurantRepository.findAll();
        //System.out.println(restaurantsFromDatabase);
        for (Restaurant restaurant : restaurantsFromDatabase) {
            System.out.println(restaurant);
        }

        // assert comprobar que sí hay restaurantes
        assertNotNull(restaurantsFromDatabase);
        assertFalse(restaurantsFromDatabase.isEmpty());
        assertEquals(2, restaurantsFromDatabase.size());

        // array[0]
        restaurantsFromDatabase.get(0);
        restaurantsFromDatabase.getFirst(); // nuevo desde java 21

        assertEquals("Restaurante 1", restaurantsFromDatabase.get(0).getName()); // primer elemento
        assertEquals("Restaurante 1", restaurantsFromDatabase.getFirst().getName()); // primer elemento
        assertEquals("Restaurante 2", restaurantsFromDatabase.get(1).getName()); // segundo elemento
    }

    @Test
    void borrarRestaurantePorId() {
        /*
    Tipos número entero en Java:
    byte
    short
    int
    long

    Por defecto usamos Long en clave primaria por eso al hacer findById o deleteById nos pide L
     */
        // DELETE FROM restaurantes WHERE id = x;
        Restaurant restaurant = new Restaurant("Restaurante 1", 20.6);
        restaurant = restaurantRepository.save(restaurant);
        Long id = restaurant.getId();

        // 1 int
        // 1L Long
        // El campo id de Restaurant es Long id, por tanto tenemos que poner 1L para que sepa que es Long
        // restaurantRepository.deleteById(1L);
        //restaurantRepository.deleteById(id);
        restaurantRepository.delete(restaurant);

        // Comprobar que se ha borrado
        assertEquals(0, restaurantRepository.count()); // SELECT COUNT(*)
    }

    @Test
    @DisplayName("Borrar todos")
    void borrarRestaurantes() {
        Restaurant restaurant1 = new Restaurant("Restaurante 1", 20.6);
        Restaurant restaurant2 = new Restaurant("Restaurante 2", 25.3);
        restaurantRepository.saveAll(List.of(restaurant1, restaurant2));
        assertEquals(2, restaurantRepository.count());

        restaurantRepository.deleteAll();
        // restaurantRepository.deleteAllInBatch();

        assertEquals(0, restaurantRepository.count());
    }

    @Test
    @DisplayName("Buscar restaurante por id que NO EXISTE")
    void buscarRestaurantePorId_Vacio() {
        // Probar a hacer una consulta y traer un restaurante por su id (WHERE id = X)

        Long id = 1L;

        // findById devuelve un Optional<Restaurant> porque el restaurante con ese id puede no existir
        Optional<Restaurant> restaurante = restaurantRepository.findById(id);

        boolean existeRestaurante = restaurante.isPresent();

        assertFalse(existeRestaurante);

    }

    @Test
    @DisplayName("Buscar restaurante por id que SÍ EXISTE")
    void buscarRestaurantePorId_Existente() {

        // Paso 1: crear y guardar un restaurante en base de datos:
        var restaurant = new Restaurant("Restaurante 1", 20.6);
        restaurant = restaurantRepository.save(restaurant);
        Long id = restaurant.getId();


        // Paso 2: Buscar el restaurante en base de datos por id (clave primaria Long en la entidad Restaurant)
        Optional<Restaurant> optional = restaurantRepository.findById(id);

        // Paso 3: comprobar que existe el restaurante buscado
        boolean existe = optional.isPresent();
        assertTrue(existe);


        // Paso 4 (opcional): obtener el restaurante del Optional y comprobar su nombre precio....
        Restaurant restaurantFromDatabase = optional.get(); // devuelve el restaurante si existe, si no existe lanza NoSuchElementException
        assertEquals("Restaurante 1", restaurantFromDatabase.getName());
        assertEquals(20.6, restaurantFromDatabase.getAveragePrice());
    }

    // exist devuelve boolean
    @Test
    void existByIdTest() {

        // buscar un restaurante que no existe
        // false
        // opción 1: assert directo
        assertFalse(restaurantRepository.existsById(1L));
        // opción 2: lo guardo en una variable primero, por si lo quiero usar en más sitios de mi programa
        boolean restauranteExiste = restaurantRepository.existsById(1L);
        assertFalse(restauranteExiste);




        // ----------------------------------------------
        // buscar un restaurante que sí existe
        // true
        Restaurant restaurant = new Restaurant("Restaurante 1", 20.6);
        restaurant = restaurantRepository.save(restaurant);

        // Que hago aquí ???? para comprobar si existe el restaurante 1 que acabo de crear usando exists
        assertTrue(restaurantRepository.existsById(1L));
        assertTrue(restaurantRepository.existsById(restaurant.getId()));

        if (restaurantRepository.existsById(1L)) {
            System.out.println("Hola crack, estamos abiertos, qué quieres comer hoy");
        } else {
            System.out.println("Lo siento, hacienda nos ha cerrado el chiringuito.");
        }

    }

    @Test
    void foodTypeEnum () {

    }
}