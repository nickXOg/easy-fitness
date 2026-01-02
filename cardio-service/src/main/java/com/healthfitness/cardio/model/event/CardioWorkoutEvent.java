package com.healthfitness.cardio.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardioWorkoutEvent {
    private UUID workoutId;
    private Long userId;
    private Double distance;
    private Integer caloriesBurned;
}
