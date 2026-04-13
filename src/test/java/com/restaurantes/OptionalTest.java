package com.restaurantes;

import com.restaurantes.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTest {

    // Mecanismo de Null Safety

    // Optional

    // of
    @Test
    void comoCrearUnOptional() {

        Employee emp1 = new Employee();
        Optional<Employee> optional = Optional.of(emp1);
    }

    // ofNullable

    // or, orElse

    // isPresent, isEmpty

    // ifPresent
}
