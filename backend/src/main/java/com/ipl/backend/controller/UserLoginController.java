package com.ipl.backend.controller;

import com.ipl.backend.dto.LoginRequest;
import com.ipl.backend.dto.LoginResponse;
import com.ipl.backend.dto.SignUpRequest;
import com.ipl.backend.dto.SignUpResponse;
import com.ipl.backend.service.UserLoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserLoginController {

    private final UserLoginService userLoginService;

    public UserLoginController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @PostMapping("/register")
    public ResponseEntity<SignUpResponse> registerUser(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = userLoginService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userLoginService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}