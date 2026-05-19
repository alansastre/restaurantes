package com.restaurantes.controller;

import com.restaurantes.dto.RegisterForm;
import com.restaurantes.model.User;
import com.restaurantes.repository.UserRepository;
import com.restaurantes.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

// controlador para iniciar sesion y/ registrarse crear User en db
@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    // GETMapping register
    // navegar a formulario de registro
    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterForm());
        return "auth/register";
    }

    // PostMapping register
    @PostMapping("register")
    public String register(@ModelAttribute RegisterForm form) {
        System.out.println(form);
        //userService.register(user);
        return "redirect:/login";
    }


}
