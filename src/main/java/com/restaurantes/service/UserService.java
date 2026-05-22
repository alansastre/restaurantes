package com.restaurantes.service;

import com.restaurantes.dto.RegisterForm;
import com.restaurantes.model.User;
import com.restaurantes.model.enums.Role;
import com.restaurantes.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // spring security invoca automáticamente este metodo al hacer login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public User register(RegisterForm form) {

        if (userRepository.existsByUsername(form.getUsername()))
            throw new IllegalArgumentException("username ya existe, elige otro username");

        if (userRepository.existsByEmail(form.getEmail()))
            throw new IllegalArgumentException("email ya existe, elige otro email");

        if (! form.getPassword().equals(form.getPasswordConfirm()))
            throw new IllegalArgumentException("Las contraseñas no coinciden");

//        if (!form.getAcceptRGPD())
//            throw new IllegalArgumentException("Debes aceptar la política de privacidad");

        User user = new User();
        user.setUsername(form.getUsername());
        user.setEmail(form.getEmail());
        user.setRole(Role.ROLE_USER);
        // user.setPassword(form.getPassword()); // password en texto plano
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        user.setPassword(encodedPassword); // $2a$10$u7/W/ivh4XDB40YBjdE9o.wTRaXFitlUrXSUorudG1IdZs/mL2DHu
        return userRepository.save(user);
    }
    // validate
    // send email
    // calculate spent money
}
