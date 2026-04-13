package com.restaurantes.repository;

import com.restaurantes.model.Employee;
import com.restaurantes.model.WorkLevel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // es una anotación de Spring Data JPA
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository repository;
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
}