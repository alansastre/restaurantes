package com.restaurantes.controller;


import com.restaurantes.dto.RegisterForm;
import com.restaurantes.model.User;
import com.restaurantes.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

// @SpringBootTest // 11s
@WebMvcTest(controllers = AuthController.class) // carga solo este controller // 4s
@AutoConfigureMockMvc(addFilters = false) // sin activar Security
public class AuthControllerTest {

    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
        verifyNoInteractions(userService);
    }
    @Test
    public void registerTest() throws Exception {
        when(userService.register(any(RegisterForm.class))).thenReturn(User.builder().username("elpepe").build());

        mockMvc.perform(post("/register")
                        .param("username", "elpepe")
                        .param("email", "elpepe@gmail.com")
                        .param("password", "user")
                        .param("passwordConfirm", "user")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("message", "Cuenta creada correctamente, inicia sesión."));

        verify(userService).register(any(RegisterForm.class));
    }
}
