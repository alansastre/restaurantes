package com.restaurantes.integration;

import com.restaurantes.model.Employee;
import com.restaurantes.model.enums.WorkLevel;
import com.restaurantes.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica que la tabla 'employees' (con la columna enum 'level') se crea y funciona en
 * PostgreSQL real.
 *
 * Antes fallaba: el `@Column(columnDefinition = "ENUM('JUNIOR','SENIOR')...")` usaba sintaxis
 * de MySQL/H2, y PostgreSQL rechazaba el CREATE TABLE con "type enum does not exist" (la tabla
 * 'employees' ni se creaba). Al mapear el enum como `varchar` + check (sin columnDefinition de
 * motor concreto), el esquema ya es portable y este test pasa contra Postgres de verdad.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeePostgresTest extends AbstractPostgresIT {

    @Autowired
    EmployeeRepository employeeRepository;

    @Test
    void guardaYLeeEmpleadoConNivelEnumEnPostgres() {
        Employee empleado = Employee.builder()
                .nif("12345678X")
                .active(true)
                .level(WorkLevel.JUNIOR)
                .build();

        Employee guardado = employeeRepository.save(empleado);
        assertNotNull(guardado.getId());

        Optional<Employee> recuperado = employeeRepository.findById(guardado.getId());
        assertTrue(recuperado.isPresent());
        assertEquals(WorkLevel.JUNIOR, recuperado.get().getLevel());
    }
}
