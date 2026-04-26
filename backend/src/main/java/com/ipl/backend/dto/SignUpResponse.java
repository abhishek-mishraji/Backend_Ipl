package com.ipl.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

    private String message;
    private Integer userId;
    private String fullName;
    private String username;
    private String email;
    private String role;
    private String token;
}