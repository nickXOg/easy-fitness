package com.healthfitness.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthfitness.auth.dto.UserDTO;
import com.healthfitness.auth.dto.UserProfileUpdateDTO;
import com.healthfitness.auth.entity.User;
import com.healthfitness.auth.repository.UserRepository;
import com.healthfitness.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetCurrentUserProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO currentUser = UserDTO.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);

        mockMvc.perform(get("/api/profile/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("email", "test@example.com"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-05-15"));
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        UUID userId = UUID.randomUUID();
        UserDTO currentUser = UserDTO.builder()
                .id(userId)
                .email("test@example.com")
                .build();

        UserProfileUpdateDTO updateDTO = UserProfileUpdateDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1992, 8, 20))
                .build();

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@example.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail("test@example.com");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Smith");
        updatedUser.setDateOfBirth(LocalDate.of(1992, 8, 20));

        UserDTO updatedUserDTO = UserDTO.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1992, 8, 20))
                .build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/profile/me")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("email", "test@example.com"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.dateOfBirth").value("1992-08-20"));
    }

    @Test
    public void testGetUserProfileById() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.of(1990, 5, 15));

        UserDTO userDTO = UserDTO.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/profile/" + userId)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("email", "test@example.com"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-05-15"));
    }

    @Test
    public void testGetUserProfileByEmail() throws Exception {
        String email = "test@example.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.of(1990, 5, 15));

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .email(email)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/profile/by-email/" + email)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("email", "test@example.com"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-05-15"));
    }
}