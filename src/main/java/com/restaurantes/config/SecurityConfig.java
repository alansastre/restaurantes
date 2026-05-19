package com.restaurantes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // passwordEncoder @Bean cifrar y verificar passwords
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // securityFilterChain @Bean proteger rutas
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // proteger las rutas
        http.authorizeHttpRequests(
                auth -> auth
                .requestMatchers("/register", "/login", "/css/**", "/webjars/**", "/images/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/restaurants/deactivate/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants/new").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants/edit/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/restaurants").permitAll()
                .requestMatchers(HttpMethod.GET, "/restaurants/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/restaurants").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/dishes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes/new").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes/edit/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/dishes").permitAll()
                .requestMatchers(HttpMethod.GET, "/dishes/*").permitAll()

                .requestMatchers(HttpMethod.GET, "/reviews").permitAll()
                .requestMatchers(HttpMethod.GET, "/reviews/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/reviews").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/reviews/new").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/reviews/edit/*").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.GET, "/reviews/disable/*").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/reviews/delete/*").hasRole("ADMIN")

                .requestMatchers("/orders", "/orders/**").authenticated()

                // lo demás autenticado si o si
                .anyRequest().authenticated()
                // ....
        );

        http.formLogin(
                form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/restaurants", true)
                        .permitAll()
        );


        return http.build();
    }

}
