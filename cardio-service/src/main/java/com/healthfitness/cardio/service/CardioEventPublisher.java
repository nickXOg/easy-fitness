package com.healthfitness.cardio.service;

import com.healthfitness.cardio.entity.CardioWorkout;
import com.healthfitness.cardio.event.CardioWorkoutEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardioEventPublisher {

    private final StreamBridge streamBridge;

    public void publishWorkoutCreated(CardioWorkout workout) {
        CardioWorkoutEvent event = new CardioWorkoutEvent(
                workout.getId(),
                workout.getUserId(),
                workout.getDistance(),
                workout.getCaloriesBurned()
        );
        streamBridge.send("cardio-workout-created", event);
    }
}
