package com.healthfitness.auth.service;

import com.healthfitness.auth.dto.RegisterRequest;
import com.healthfitness.auth.dto.UserDTO;
import com.healthfitness.auth.entity.Role;
import com.healthfitness.auth.entity.User;
import com.healthfitness.auth.repository.RoleRepository;
import com.healthfitness.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(roleRepository.findByName(Role.ERole.ROLE_USER)).thenReturn(Optional.of(new Role(1, Role.ERole.ROLE_USER)));
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserDTO result = authService.registerUser(registerRequest);

        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void registerUser_emailExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@test.com");

        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            authService.registerUser(registerRequest);
        });
    }
}
