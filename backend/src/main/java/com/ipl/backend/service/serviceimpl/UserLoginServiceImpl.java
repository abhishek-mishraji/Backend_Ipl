package com.ipl.backend.service.serviceimpl;

import com.ipl.backend.dto.LoginRequest;
import com.ipl.backend.dto.LoginResponse;
import com.ipl.backend.dto.SignUpRequest;
import com.ipl.backend.dto.SignUpResponse;
import com.ipl.backend.entity.User;
import com.ipl.backend.jwt.JwtUtil;
import com.ipl.backend.repository.UserRepository;
import com.ipl.backend.service.UserLoginService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserLoginServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public SignUpResponse registerUser(SignUpRequest request) {
        String fullName = request.getFullName().trim();
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(email);
        user.setRole("USER");

        try {
            User savedUser = userRepository.save(user);
            String token = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole());

            return new SignUpResponse(
                    "Signup successful",
                    savedUser.getUserId(),
                    savedUser.getFullName(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getRole(),
                    token);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username or email already exists");
        }
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        String username = request.getUsername().trim();

        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new LoginResponse(
                "Login successful",
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                token);
    }
}