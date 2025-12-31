package com.healthfitness.auth.controller;

import com.healthfitness.auth.dto.UserDTO;
import com.healthfitness.auth.dto.UserProfileUpdateDTO;
import com.healthfitness.auth.entity.User;
import com.healthfitness.auth.message.UserDataMessage;
import com.healthfitness.auth.repository.UserRepository;
import com.healthfitness.auth.service.UserDataPublisher;
import com.healthfitness.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users/profile")
public class ProfileController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserDataPublisher userDataPublisher;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestBody UserProfileUpdateDTO profileUpdateDTO) {
        UserDTO currentUser = userService.getCurrentUser();
        Optional<User> userOptional = userRepository.findById(currentUser.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Update user fields (excluding email since that's tied to Keycloak)
            user.setFirstName(profileUpdateDTO.getFirstName());
            user.setLastName(profileUpdateDTO.getLastName());
            user.setDateOfBirth(profileUpdateDTO.getDateOfBirth());
            User updatedUser = userRepository.save(user);
            // Publish user data to Kafka for profile updates
            UserDataMessage userDataMessage = UserDataMessage.builder()
                    .keycloakId(updatedUser.getKeycloakId().toString())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .dateOfBirth(updatedUser.getDateOfBirth())
                    .build();
            userDataPublisher.publishUserData(userDataMessage);
            UserDTO updatedUserDTO = UserDTO.builder()
                    .id(updatedUser.getId())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .dateOfBirth(updatedUser.getDateOfBirth())
                    .build();
            return ResponseEntity.ok(updatedUserDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dateOfBirth(user.getDateOfBirth())
                    .build();
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserDTO> getUserProfileByEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dateOfBirth(user.getDateOfBirth())
                    .build();
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-keycloak-id/{keycloakId}")
    public ResponseEntity<UserDTO> getUserProfileByKeycloakId(@PathVariable String keycloakId) {
        log.info("Checking user with keycloakId='{}'", keycloakId);  // Лог 1
        try {
            UUID uuid = UUID.fromString(keycloakId);
            log.info("Converted to UUID: {}", uuid);  // Лог 2
            Optional<User> userOptional = userRepository.findByKeycloakId(uuid);
            log.info("User found: {}", userOptional.isPresent());  // Лог 3
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserDTO userDTO = UserDTO.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .build();
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", keycloakId, e);
            return ResponseEntity.badRequest().build();
        }
    }
}