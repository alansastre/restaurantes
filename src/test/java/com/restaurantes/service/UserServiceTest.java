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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

    @Test
    void loadUserByUsernameOK() {

        User pepe = User.builder().username("elpepe").role(Role.ROLE_USER).build();
        Optional<User> pepeOptional = Optional.of(pepe);
        when(userRepository.findByUsername("elpepe")).thenReturn(pepeOptional);

        UserDetails userDB = userService.loadUserByUsername("elpepe");

        assertNotNull(userDB);
        assertEquals("elpepe", userDB.getUsername());
        userDB.getAuthorities().forEach(auth -> assertEquals("ROLE_USER", auth.getAuthority()));
        verify(userRepository).findByUsername("elpepe");
    }

    @Test
    void loadUserByUsernameException() {
        when(userRepository.findByUsername("ko")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("ko"));

        verify(userRepository).findByUsername("ko");
    }

    @Test
    void registerUsernameNotAvailable() {
        when(userRepository.existsByUsername("ocupado")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.register(RegisterForm.builder().username("ocupado").build()));

        assertEquals("username ya existe, elige otro username", exception.getMessage());

        verify(userRepository).existsByUsername("ocupado");

        verify(userRepository, never()).save(any(User.class));

        verifyNoInteractions(passwordEncoder);

    }
    @Test
    void registerEmailNotAvailable() {
        when(userRepository.existsByUsername("libre")).thenReturn(false);
        when(userRepository.existsByEmail("ocupado@gmail.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(
                        RegisterForm.builder().username("libre").email("ocupado@gmail.com").build()
                )
        );

        assertEquals("email ya existe, elige otro email", exception.getMessage());

        verify(userRepository).existsByUsername("libre");
        verify(userRepository).existsByEmail("ocupado@gmail.com");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder);
    }
    @Test
    void registerPasswordNotMatch() {
        when(userRepository.existsByUsername("libre")).thenReturn(false);
        when(userRepository.existsByEmail("libre@gmail.com")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(
                        RegisterForm.builder().username("libre").email("libre@gmail.com")
                                .password("abcd").passwordConfirm("dcba").build()
                )
        );
        assertEquals("Las contraseñas no coinciden", exception.getMessage());

        verify(userRepository).existsByUsername("libre");
        verify(userRepository).existsByEmail("libre@gmail.com");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(passwordEncoder);
    }

}