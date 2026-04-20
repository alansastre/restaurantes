package com.restaurantes.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    // http://localhost:8080/hola
    @GetMapping("hola")
    public String hola() {
        return "hola";
    }
}
