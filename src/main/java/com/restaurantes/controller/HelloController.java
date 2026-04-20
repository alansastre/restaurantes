package com.restaurantes.controller;


import com.restaurantes.model.Restaurant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HelloController {

    // http://localhost:8080/hola
    @GetMapping("hola")
    public String hola() {
        return "hola";
    }
    // Crear un metodo /adios que tenga Model model como parámetro
    // model.addAttribute("mensaje", "Adiós mundo cruel");
    @GetMapping("adios")
    public String adios(Model model) {
        model.addAttribute("mensaje", "Hola desde Java");
        model.addAttribute("dinerito", 30.50);
        return "adios";
    }
}
