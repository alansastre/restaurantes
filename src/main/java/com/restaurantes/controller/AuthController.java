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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// controlador para iniciar sesion y/ registrarse crear User en db
@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute RegisterForm form, RedirectAttributes redirectAttributes) {
        try {
            userService.register(form);
            redirectAttributes.addFlashAttribute("message", "Cuenta creada correctamente, inicia sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // Bloquear tras X intentos de login fallido
    // https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
    @GetMapping("login")
    public String login() {
        return "auth/login";
    }



}
