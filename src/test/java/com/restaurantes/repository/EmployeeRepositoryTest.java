package com.restaurantes.repository;

import com.restaurantes.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

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
        Employee employee = new Employee();
        employee.setNif("1A");
    }





    @Test
    void deleteById() {
    }
}