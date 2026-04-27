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


    // mapping
    @GetMapping("dishes/{id}")
    public String dishDetail(Model model, @PathVariable Long id) {

        model.addAttribute("dish", dishRepository.findById(id).orElseThrow());

        return "dishes/dish-detail";
    }
}
