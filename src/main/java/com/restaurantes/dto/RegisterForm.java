package com.restaurantes.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Setter
@ToString
public class RegisterForm {

    private String username;
    private String email;
    private String password;
    private String passwordConfirm;
//     private Boolean acceptRGPD;
}
