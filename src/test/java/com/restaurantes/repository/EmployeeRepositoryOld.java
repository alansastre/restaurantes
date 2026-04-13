package com.restaurantes.repository;

import com.restaurantes.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests para EmployeeRepository - Operaciones CRUD")
class EmployeeRepositoryOld {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Inicializar datos de prueba
        employee1 = new Employee();
        employee1.setNif("12345678A");
        employee1.setActive(true);

        employee2 = new Employee();
        employee2.setNif("87654321B");
        employee2.setActive(true);
    }

    // ============ TESTS DE CREATE (Crear) ============

    @Test
    @DisplayName("Crear un nuevo empleado exitosamente")
    void testCreateEmployee() {
        // Given: Un nuevo empleado
        Employee newEmployee = new Employee();
        newEmployee.setNif("99999999C");
        newEmployee.setActive(true);

        // When: Guardamos el empleado
        Employee savedEmployee = employeeRepository.save(newEmployee);

        // Then: El empleado debe guardarse con un ID generado
        assertNotNull(savedEmployee.getId());
        assertTrue(savedEmployee.getId() > 0);
        assertEquals("99999999C", savedEmployee.getNif());
    }

    @Test
    @DisplayName("Crear múltiples empleados exitosamente")
    void testCreateMultipleEmployees() {
        // Given: Dos empleados nuevos
        // When: Guardamos ambos empleados
        Employee saved1 = employeeRepository.save(employee1);
        Employee saved2 = employeeRepository.save(employee2);

        // Then: Ambos deben guardarse exitosamente
        assertNotNull(saved1.getId());
        assertNotNull(saved2.getId());
        assertEquals("12345678A", saved1.getNif());
        assertEquals("87654321B", saved2.getNif());
    }

    @Test
    @DisplayName("Crear empleado con valores por defecto")
    void testCreateEmployeeWithDefaults() {
        // Given: Un nuevo empleado minimal
        Employee minimalEmployee = new Employee();
        minimalEmployee.setNif("11111111D");

        // When: Guardamos el empleado
        Employee saved = employeeRepository.save(minimalEmployee);

        // Then: Debe guardarse con el NIF especificado
        assertNotNull(saved.getId());
        assertEquals("11111111D", saved.getNif());
    }

    // ============ TESTS DE READ (Leer) ============

    @Test
    @DisplayName("Obtener un empleado por ID exitosamente")
    void testReadEmployeeById() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);

        // When: Buscamos el empleado por su ID
        Optional<Employee> found = employeeRepository.findById(saved.getId());

        // Then: Debe encontrar el empleado
        assertTrue(found.isPresent());
        assertEquals("12345678A", found.get().getNif());
        assertTrue(found.get().getActive());
    }

    @Test
    @DisplayName("Obtener un empleado que no existe devuelve Optional vacío")
    void testReadEmployeeByIdNotFound() {
        // Given: Un ID que no existe
        Long nonExistentId = 999L;

        // When: Buscamos un empleado con ese ID
        Optional<Employee> found = employeeRepository.findById(nonExistentId);

        // Then: No debe encontrar nada
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Obtener todos los empleados")
    void testReadAllEmployees() {
        // Given: Guardamos dos empleados
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // When: Obtenemos todos los empleados
        List<Employee> employees = employeeRepository.findAll();

        // Then: Debe haber al menos 2 empleados
        assertNotNull(employees);
        assertTrue(employees.size() >= 2);
    }

    @Test
    @DisplayName("Obtener todos los empleados cuando está vacío")
    void testReadAllEmployeesEmpty() {
        // Given: Repositorio vacío
        employeeRepository.deleteAll();

        // When: Obtenemos todos los empleados
        List<Employee> employees = employeeRepository.findAll();

        // Then: Debe retornar una lista vacía
        assertNotNull(employees);
        assertTrue(employees.isEmpty());
    }

    @Test
    @DisplayName("Contar el número total de empleados")
    void testCountEmployees() {
        // Given: Guardamos dos empleados
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // When: Contamos los empleados
        long count = employeeRepository.count();

        // Then: Debe haber al menos 2 empleados
        assertTrue(count >= 2);
    }

    // ============ TESTS DE UPDATE (Actualizar) ============

    @Test
    @DisplayName("Actualizar un empleado existente")
    void testUpdateEmployee() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);
        Long employeeId = saved.getId();

        // When: Actualizamos sus datos
        saved.setActive(false);
        Employee updated = employeeRepository.save(saved);

        // Then: Los cambios deben persistir
        assertEquals(employeeId, updated.getId());
        assertFalse(updated.getActive());
    }

    @Test
    @DisplayName("Actualizar el NIF de un empleado")
    void testUpdateEmployeeNif() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);

        // When: Actualizamos el NIF
        saved.setNif("55555555E");
        Employee updated = employeeRepository.save(saved);

        // Then: El NIF debe estar actualizado
        assertEquals("55555555E", updated.getNif());
    }

    @Test
    @DisplayName("Actualizar múltiples propiedades de un empleado")
    void testUpdateMultipleProperties() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);

        // When: Actualizamos múltiples propiedades
        saved.setNif("66666666F");
        saved.setActive(false);
        Employee updated = employeeRepository.save(saved);

        // Then: Todos los cambios deben persistir
        assertEquals("66666666F", updated.getNif());
        assertFalse(updated.getActive());
    }

    @Test
    @DisplayName("Verificar que la actualización no cambia el ID")
    void testUpdateDoesNotChangeId() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);
        Long originalId = saved.getId();

        // When: Actualizamos el empleado
        saved.setNif("77777777G");
        Employee updated = employeeRepository.save(saved);

        // Then: El ID no debe cambiar
        assertEquals(originalId, updated.getId());
    }

    // ============ TESTS DE DELETE (Eliminar) ============

    @Test
    @DisplayName("Eliminar un empleado por ID")
    void testDeleteEmployeeById() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);
        Long employeeId = saved.getId();

        // When: Eliminamos el empleado
        employeeRepository.deleteById(employeeId);

        // Then: El empleado no debe existir
        Optional<Employee> found = employeeRepository.findById(employeeId);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Eliminar un empleado mediante la entidad")
    void testDeleteEmployee() {
        // Given: Un empleado guardado
        Employee saved = employeeRepository.save(employee1);
        Long employeeId = saved.getId();

        // When: Eliminamos el empleado
        employeeRepository.delete(saved);

        // Then: El empleado no debe existir
        Optional<Employee> found = employeeRepository.findById(employeeId);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Eliminar múltiples empleados")
    void testDeleteMultipleEmployees() {
        // Given: Dos empleados guardados
        Employee saved1 = employeeRepository.save(employee1);
        Employee saved2 = employeeRepository.save(employee2);

        // When: Eliminamos ambos empleados
        employeeRepository.delete(saved1);
        employeeRepository.delete(saved2);

        // Then: Ninguno debe existir
        assertFalse(employeeRepository.findById(saved1.getId()).isPresent());
        assertFalse(employeeRepository.findById(saved2.getId()).isPresent());
    }

    @Test
    @DisplayName("Eliminar todos los empleados")
    void testDeleteAllEmployees() {
        // Given: Varios empleados guardados
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // When: Eliminamos todos los empleados
        employeeRepository.deleteAll();

        // Then: No debe haber ningún empleado
        long count = employeeRepository.count();
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Intentar eliminar un empleado que no existe no genera error")
    void testDeleteNonExistentEmployee() {
        // Given: Un ID que no existe
        Long nonExistentId = 999L;

        // When/Then: Eliminar un empleado inexistente no debe lanzar excepción
        assertDoesNotThrow(() -> employeeRepository.deleteById(nonExistentId));
    }

    // ============ TESTS ADICIONALES ============

    @Test
    @DisplayName("Verificar que el repositorio implementa JpaRepository")
    void testRepositoryIsJpaRepository() {
        // Given: El repositorio debe existir
        assertNotNull(employeeRepository);

        // Then: Debe ser una instancia de EmployeeRepository
        assertTrue(employeeRepository instanceof EmployeeRepository);
    }

    @Test
    @DisplayName("Crear y verificar persistencia en un ciclo completo")
    void testCompleteLifeCycle() {
        // CREATE: Crear un empleado
        Employee newEmployee = new Employee();
        newEmployee.setNif("88888888H");
        newEmployee.setActive(true);
        Employee saved = employeeRepository.save(newEmployee);

        // READ: Verificar que se guardó
        Optional<Employee> found = employeeRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // UPDATE: Actualizar el empleado
        found.get().setActive(false);
        Employee updated = employeeRepository.save(found.get());
        assertEquals(false, updated.getActive());

        // DELETE: Eliminar el empleado
        employeeRepository.deleteById(updated.getId());
        assertFalse(employeeRepository.findById(updated.getId()).isPresent());
    }
}


