package com.restaurantes.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
// TODO IMPORTS


// TODO MOCKMVC
// TODO TRANSACTIONAL
@SpringBootTest
public class DishControllerTest {
    // TODO MOCKMVC
    // TODO REPOS
//    @Test
//    void list() {
//        Assertions.fail("Pendiente test detail");
//    }
//    @Test
//    void detail() {
//        Assertions.fail("Pendiente test detail");
//    }
    @Test
    void create() {
        // count

        // mockmvc perform post /dishes

        // count + 1
        // findById y comprobar datos
        Assertions.fail("Pendiente test crear nuevo Plato");
    }
    @Test
    void edit() {
        // save

        // mockmvc perform post cambiando nombre precio restaurante apuntando al mismo id /dishes

        // findById y comprobar datos
        // count no debe aumentar
        Assertions.fail("Pendiente test editar Plato existente");
    }
}