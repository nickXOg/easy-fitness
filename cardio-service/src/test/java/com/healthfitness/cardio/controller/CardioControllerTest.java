package com.healthfitness.cardio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthfitness.cardio.model.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.service.CardioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CardioController.class)
class CardioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardioService cardioService;

    @Test
    void shouldCreateWorkoutSuccessfully() throws Exception {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        CardioWorkoutDTO inputDto = new CardioWorkoutDTO();
        inputDto.setUserId(1L);
        inputDto.setWorkoutTypeId(workoutTypeId);
        inputDto.setDistance(5.0);
        inputDto.setDuration(30);
        inputDto.setCaloriesBurned(300);
        inputDto.setStartTime(LocalDateTime.now());
        inputDto.setEndTime(LocalDateTime.now().plusMinutes(30));

        CardioWorkoutDTO outputDto = new CardioWorkoutDTO();
        outputDto.setId(UUID.randomUUID());
        outputDto.setUserId(1L);
        outputDto.setWorkoutTypeId(workoutTypeId);
        outputDto.setDistance(5.0);
        outputDto.setDuration(30);
        outputDto.setCaloriesBurned(300);
        outputDto.setStartTime(inputDto.getStartTime());
        outputDto.setEndTime(inputDto.getEndTime());

        when(cardioService.createWorkout(any(CardioWorkoutDTO.class))).thenReturn(outputDto);

        // When & Then
        mockMvc.perform(post("/api/cardio/workouts")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("user_id", "1"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(outputDto.getId().toString()))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.distance").value(5.0))
                .andExpect(jsonPath("$.duration").value(30))
                .andExpect(jsonPath("$.caloriesBurned").value(300));
    }

    @Test
    void shouldGetWorkoutsByUserIdSuccessfully() throws Exception {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        UUID workoutId1 = UUID.randomUUID();
        UUID workoutId2 = UUID.randomUUID();
        
        CardioWorkoutDTO workout1 = new CardioWorkoutDTO();
        workout1.setId(workoutId1);
        workout1.setUserId(1L);
        workout1.setWorkoutTypeId(workoutTypeId);
        workout1.setDistance(5.0);
        workout1.setDuration(30);
        workout1.setCaloriesBurned(300);
        workout1.setStartTime(LocalDateTime.now());

        CardioWorkoutDTO workout2 = new CardioWorkoutDTO();
        workout2.setId(workoutId2);
        workout2.setUserId(1L);
        workout2.setWorkoutTypeId(workoutTypeId);
        workout2.setDistance(10.0);
        workout2.setDuration(60);
        workout2.setCaloriesBurned(600);
        workout2.setStartTime(LocalDateTime.now().plusDays(1));

        List<CardioWorkoutDTO> workouts = Arrays.asList(workout1, workout2);
        when(cardioService.getWorkoutsByUserId(1L)).thenReturn(workouts);

        // When & Then
        mockMvc.perform(get("/api/cardio/workouts/user/1")
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                        .jwt(jwt -> jwt.subject("test-user").claim("user_id", "1"))
                        .authorities(new SimpleGrantedAuthority("ROLE_easy-fitness.user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(workoutId1.toString()))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].distance").value(5.0))
                .andExpect(jsonPath("$[0].duration").value(30))
                .andExpect(jsonPath("$[0].caloriesBurned").value(300))
                .andExpect(jsonPath("$[1].id").value(workoutId2.toString()))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].distance").value(10.0))
                .andExpect(jsonPath("$[1].duration").value(60))
                .andExpect(jsonPath("$[1].caloriesBurned").value(600));
    }
}