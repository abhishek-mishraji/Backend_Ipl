package com.ipl.backend.service;

import com.ipl.backend.dto.LoginRequest;
import com.ipl.backend.dto.LoginResponse;
import com.ipl.backend.dto.SignUpRequest;
import com.ipl.backend.dto.SignUpResponse;

public interface UserLoginService {

    SignUpResponse registerUser(SignUpRequest request);

    LoginResponse loginUser(LoginRequest request);
}