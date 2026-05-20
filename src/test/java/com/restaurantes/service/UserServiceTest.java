package com.restaurantes.service;

import com.restaurantes.dto.RegisterForm;
import com.restaurantes.model.User;
import com.restaurantes.model.enums.Role;
import com.restaurantes.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//Test unitario con mocks
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void registerOK() {
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("user@gmail.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("user")).thenReturn("encodedPassword");

        var form = RegisterForm.builder()
                .email("user@gmail.com")
                .username("user")
                .password("user")
                .passwordConfirm("user")
                .build();

        User user = userService.register(form);
        assertNotNull(user);
        assertEquals("user@gmail.com", user.getEmail());
        assertEquals("user", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());

        verify(userRepository).existsByUsername("user");
        verify(userRepository).existsByEmail("user@gmail.com");
        verify(passwordEncoder).encode("user");
        verify(userRepository).save(any(User.class));
    }

    // username ocupado

    // email ocupado

    // passwords no coinciden

    // find by user name
}