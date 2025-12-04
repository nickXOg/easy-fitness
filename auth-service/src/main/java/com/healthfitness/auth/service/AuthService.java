package com.healthfitness.auth.service;

import com.healthfitness.auth.dto.JwtResponse;
import com.healthfitness.auth.dto.LoginRequest;
import com.healthfitness.auth.dto.RegisterRequest;
import com.healthfitness.auth.dto.UserDTO;
import com.healthfitness.auth.entity.Role;
import com.healthfitness.auth.entity.User;
import com.healthfitness.auth.repository.RoleRepository;
import com.healthfitness.auth.repository.UserRepository;
import com.healthfitness.auth.security.JwtUtils;
import com.healthfitness.auth.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponse loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return JwtResponse.builder()
                .token(jwt)
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    public UserDTO registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(encoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .build();

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(roles.stream().map(role -> role.getName().name()).collect(Collectors.toList()))
                .build();
    }
}
