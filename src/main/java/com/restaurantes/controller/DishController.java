package com.restaurantes.controller;

import com.restaurantes.repository.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// anotaciones
@Controller
@AllArgsConstructor
public class DishController {

    // inyectar repositorios
    private final DishRepository dishRepository;
    // TODO inyectar repositorio de ReviewRepository


    // mapping
    @GetMapping("dishes/{id}")
    public String dishDetail(Model model, @PathVariable Long id) {

        model.addAttribute("dish", dishRepository.findById(id).orElseThrow());

        // TODO traer reviews de este dish por dish id

        return "dishes/dish-detail";
    }
}
