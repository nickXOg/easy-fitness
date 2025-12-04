package com.healthfitness.cardio.controller;

import com.healthfitness.cardio.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.service.CardioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardio")
@RequiredArgsConstructor
public class CardioController {

    private final CardioService cardioService;

    @PostMapping("/workouts")
    public ResponseEntity<CardioWorkoutDTO> createWorkout(@RequestBody CardioWorkoutDTO cardioWorkoutDTO) {
        CardioWorkoutDTO createdWorkout = cardioService.createWorkout(cardioWorkoutDTO);
        return ResponseEntity.ok(createdWorkout);
    }

    @GetMapping("/workouts/user/{userId}")
    public ResponseEntity<List<CardioWorkoutDTO>> getUserWorkouts(@PathVariable Long userId) {
        List<CardioWorkoutDTO> workouts = cardioService.getWorkoutsByUserId(userId);
        return ResponseEntity.ok(workouts);
    }
}
