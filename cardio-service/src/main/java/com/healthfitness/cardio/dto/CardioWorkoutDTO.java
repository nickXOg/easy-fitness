package com.healthfitness.cardio.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CardioWorkoutDTO {
    private UUID id;
    private Long userId;
    private UUID workoutTypeId;
    private Double distance;
    private Integer duration;
    private Integer averageHeartRate;
    private Integer maxHeartRate;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;
}
