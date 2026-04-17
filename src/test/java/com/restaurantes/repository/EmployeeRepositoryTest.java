package com.restaurantes.repository;

import com.restaurantes.model.Employee;
import com.restaurantes.model.Restaurant;
import com.restaurantes.model.enums.WorkLevel;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // es una anotación de Spring Data JPA
class EmployeeRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    EmployeeRepository repository;

    // Spring Data JPA ( Repository) --> EntityManager --> JDBC --> Base de datos
    // Agregamos EntityManager para poder limpiar la memoria y forzar consultas a base de datos
    @Autowired
    EntityManager entityManager;

    // declarar datos para el test
    Employee empleado1 ;
    Employee empleado2;


    @BeforeEach // se ejecuta antes de cada test
    void setUp() {
        System.out.println("Ejecutando before each");
        // inicializar datos para el test
        empleado1 = new Employee();
        empleado1.setNif("1A");
        empleado1.setActive(true);
        repository.save(empleado1);

        empleado2 = Employee.builder().nif("2B").active(false).build();
        repository.save(empleado2);
    }

    @Test
    void count() {
        assertEquals(2, repository.count());
    }
    @Test
    void existsById() {
        assertTrue(repository.existsById(empleado1.getId()));
    }
    @Test
    void findById() {
        Optional<Employee> employeeOptional=  repository.findById(1L);
        assertTrue(employeeOptional.isPresent());

        Employee employee = employeeOptional.get();
        assertEquals("1A", employee.getNif());
    }

    @Test
    void findAll() {

        List<Employee> empleados = repository.findAll();
        assertNotNull(empleados);
        // assertEquals(2, empleados.size());
        assertTrue(empleados.size() >= 2);

    }
    @Test
    void save() {
        // usar datos ya cargados
        Employee empleado = new Employee();
        empleado.setNif("3");
        repository.save(empleado);
        assertNotNull(empleado.getId()); // se le asigna automáticamente id 3
        assertTrue(repository.existsById(empleado.getId()));
    }


    @Test
    void saveAll() {
        // opción clásica, crear una lista mutable (new)
        // Lista de nombres
        String[] nombres = new String[] {"4C", "5D", "6E"}; // array de nombres
        List<String> nombresBien = new ArrayList<>();
        nombresBien.add("Juanito");
        nombresBien.add("Ruperta");

        List<Double> dineros = new ArrayList<>();
        dineros.add(5d);
        dineros.add(6d);


        Employee empleado3 = new Employee();
        Employee empleado4 = new Employee();
        List<Employee> empleados = new ArrayList<>();
        empleados.add(empleado3);
        empleados.add(empleado4);
        repository.saveAll(empleados);

        assertEquals(4,  repository.count());
        // Map<String, List<Employee>> map = new HashMap<>();
    }





    @Test
    void deleteById() {



    // comprobar que sí existe el empleado 1: existById
        assertTrue(repository.existsById(1L));
        long numeroEmpleadosAntes = repository.count();

        // borrarlo: deleteById o delete
        repository.deleteById(1L);

        // comprobar que NO existe el empleado 1
        assertFalse(repository.existsById(1L));
        long numeroEmpleadosDespues = repository.count();
        assertEquals(numeroEmpleadosAntes - 1, numeroEmpleadosDespues);

    }

    @Test
    void workLevelEnum() {
        // junior
        Employee empleado = new Employee();
        empleado.setLevel(WorkLevel.JUNIOR);

        Employee empleadoGuardado = repository.save(empleado);
        assertNotNull(empleadoGuardado.getLevel()); // compruebo que level no es null
        assertEquals(WorkLevel.JUNIOR, empleadoGuardado.getLevel()); // compruebo que el level es JUNIOR


        // senior por defecto
        Employee empleadoSenior = new Employee();
        Employee seniorGuardado = repository.save(empleadoSenior);
        // PROBAR A QUITAR LO DE QUE SEA SENIOR POR DEFECTO PARA VER CÓMO FALLA LA COMPARACIÓN
        assertEquals(WorkLevel.SENIOR, seniorGuardado.getLevel());
    }

    @Test
    void startDate() {

        // probar fecha por defecto startDate
        // crear objeto employee con new o builder
        // Given
        Employee empleado = new Employee(); // En la clase Employee tenemos puesta startDate por defecto a now()
        empleado.setNif("7F");

        // When
        Employee empleadoGuardado = repository.save(empleado);

        // Then
        // assert startDate not null
        assertNotNull(empleadoGuardado.getStartDate());
        assertEquals(LocalDate.now(), empleadoGuardado.getStartDate());

        // Probar a cambiar fecha y ver si funciona
        empleadoGuardado.setStartDate(LocalDate.of(2026, 6, 24));
        empleadoGuardado = repository.save(empleadoGuardado);
        assertEquals(LocalDate.of(2026, 6, 24), empleadoGuardado.getStartDate());


        empleadoGuardado.setStartDate(LocalDate.now().plusDays(30));
        empleadoGuardado = repository.save(empleadoGuardado);
        assertEquals(LocalDate.now().plusDays(30), empleadoGuardado.getStartDate());

        // CUIDADO con el metodo builder pone a null todos los atributos que no le pasemos
        // var empleado = Employee.builder().nif("hola").build();
    }

    // asociacion ManyToOne
    /*
    probar en h2-console:

    INSERT INTO restaurantes (name) values ('pizza');

INSERT INTO employees (nif, restaurant_id) values ('1A', 1);

SELECT * FROM employees;

DELETE FROM restaurantes WHERE ID = 1;

     */

    @Test
    void manyToOneRestaurantTest() {
        // paso 1: crear un restaurante y guardarlo
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Pizza Hut");
        restaurantRepository.save(restaurant);


        // paso 2: crear un empleado y asociarle el restaurante
        Employee empleado = new Employee();
        empleado.setNif("8G");
        empleado.setRestaurant(restaurant);
        Employee empleadoGuardado = repository.save(empleado);

        assertNotNull(empleadoGuardado.getRestaurant());

        // limpiar la memoria para forzar la consulta de findById a base de datos
        // si no hace esto, entonces nos devuelve el empleado que ya tiene en memoria sin hacer el select
        entityManager.flush(); // sincronizar cambios pendientes con la base de datos
        entityManager.clear(); // limpiar la memoria

        // buscar el empleado de la base datos para ver si viene con el Restaurante
        // Optional<Employee> empleadoOptional = repository.findById(empleadoGuardado.getId());
        Employee empleadoDB = repository.findById( empleado.getId() ).get();
        assertNotNull(empleadoDB.getRestaurant());
        System.out.println(empleadoDB);
        System.out.println(empleadoDB.getRestaurant());
    }


    // filtros basico
    // filtrar empleados por nivel JUNIOR
    @Test
    void findByLevel() {

        repository.deleteAll();
        repository.save(Employee.builder().nif("1").level(WorkLevel.JUNIOR).build());
        repository.save(Employee.builder().nif("2").level(WorkLevel.JUNIOR).build());
        repository.save(Employee.builder().nif("3").level(WorkLevel.SENIOR).build());
        repository.save(Employee.builder().nif("4").level(WorkLevel.SENIOR).build());


        List<Employee> juniors = repository.findAllByLevel(WorkLevel.JUNIOR);
        assertEquals(2, juniors.size());

        // FILTRO CON JAVa NO SERÍA LO MÁS OPTIMO mejor que filtre la base de datos
//        for (var empleado : repository.findAll())
//            if(empleado.getLevel() == WorkLevel.JUNIOR)
//                System.out.println(empleado);
//
//        repository.findAll().stream().filter(e -> e.getLevel() == WorkLevel.JUNIOR).forEach(System.out::println);
//
    }


    // Filtrar employees active = true y trabajen en DominosPizza


    @Test
    void findAllBy_ActiveTrue_And_RestaurantName() {
        repository.deleteAll();
        // paso 1: crear dos restaurantes
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("El Buen Sabor");
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = restaurantRepository.save(Restaurant.builder().name("El Mal Sabor").build());

        // paso 2: crear cuatro empleados, dos cada restaurante
        Employee empleado1 = new Employee();
        empleado1.setNif("1");
        empleado1.setActive(true);
        empleado1.setRestaurant(restaurant1);
        repository.save(empleado1);

        Employee empleado2 = repository.save(
                Employee.builder().nif("2").active(true).restaurant(restaurant1).build()
        );
        Employee empleado3 = repository.save(
                Employee.builder().nif("3").active(true).restaurant(restaurant2).build()
        );
        Employee empleado4 = repository.save(
                Employee.builder().nif("4").active(false).restaurant(restaurant2).build()
        );

        // paso 3: invocar el nuevo metodo findAllBy......
        List<Employee> empleadosActivosDelMalSabor = repository.findAllByActiveTrueAndRestaurantName("El Mal Sabor");

        // paso 4: assert
        assertEquals(1, empleadosActivosDelMalSabor.size());
    }

    // antiguedad en dias
    @Test
    void antiguedadEnDiasTest() {
        Employee empleado = new Employee();
        empleado.setNif("11A");
        empleado.setStartDate(LocalDate.of(2026, 1, 1));
        repository.save(empleado);

        Duration days = repository.findWorkDaysByNif("11A");
        System.out.println(days.toDays());

        long antiguedadEnDias = ChronoUnit.DAYS.between(empleado.getStartDate(), LocalDate.now());
        System.out.println(antiguedadEnDias);


    }
}