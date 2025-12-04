package com.healthfitness.cardio.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cardio_workouts")
@Data
public class CardioWorkout {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "workout_type_id")
    private WorkoutType workoutType;

    private Double distance;
    private Integer duration;
    private Integer averageHeartRate;
    private Integer maxHeartRate;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String notes;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
