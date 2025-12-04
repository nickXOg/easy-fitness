package com.healthfitness.auth.controller;

import com.healthfitness.auth.dto.JwtResponse;
import com.healthfitness.auth.dto.LoginRequest;
import com.healthfitness.auth.dto.RegisterRequest;
import com.healthfitness.auth.dto.UserDTO;
import com.healthfitness.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        UserDTO userDTO = authService.registerUser(registerRequest);
        return ResponseEntity.ok(userDTO);
    }
}
