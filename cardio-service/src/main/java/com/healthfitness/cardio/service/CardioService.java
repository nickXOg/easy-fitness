package com.healthfitness.cardio.service;

import com.healthfitness.cardio.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.entity.CardioWorkout;
import com.healthfitness.cardio.entity.WorkoutType;
import com.healthfitness.cardio.repository.CardioWorkoutRepository;
import com.healthfitness.cardio.repository.WorkoutTypeRepository;
import lombok.RequiredArgsConstructor;
import com.healthfitness.cardio.mapper.CardioWorkoutMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardioService {

    private final CardioWorkoutRepository cardioWorkoutRepository;
    private final WorkoutTypeRepository workoutTypeRepository;
    private final CardioEventPublisher cardioEventPublisher;
    private final CardioWorkoutMapper mapper;

    public CardioWorkoutDTO createWorkout(CardioWorkoutDTO cardioWorkoutDTO) {
        CardioWorkout cardioWorkout = new CardioWorkout();
        // map dto to entity
        cardioWorkout.setUserId(cardioWorkoutDTO.getUserId());
        WorkoutType workoutType = workoutTypeRepository.findById(cardioWorkoutDTO.getWorkoutTypeId()).orElseThrow(() -> new RuntimeException("Workout type not found"));
        cardioWorkout.setWorkoutType(workoutType);
        cardioWorkout.setDistance(cardioWorkoutDTO.getDistance());
        cardioWorkout.setDuration(cardioWorkoutDTO.getDuration());
        cardioWorkout.setAverageHeartRate(cardioWorkoutDTO.getAverageHeartRate());
        cardioWorkout.setMaxHeartRate(cardioWorkoutDTO.getMaxHeartRate());
        cardioWorkout.setCaloriesBurned(cardioWorkoutDTO.getCaloriesBurned());
        cardioWorkout.setStartTime(cardioWorkoutDTO.getStartTime());
        cardioWorkout.setEndTime(cardioWorkoutDTO.getEndTime());
        cardioWorkout.setLocation(cardioWorkoutDTO.getLocation());
        cardioWorkout.setNotes(cardioWorkoutDTO.getNotes());

        CardioWorkout savedWorkout = cardioWorkoutRepository.save(cardioWorkout);
        cardioEventPublisher.publishWorkoutCreated(savedWorkout);
        return mapper.cardioWorkoutToDto(savedWorkout);
    }

    public List<CardioWorkoutDTO> getWorkoutsByUserId(Long userId) {
        return cardioWorkoutRepository.findByUserId(userId).stream()
                .map(mapper::cardioWorkoutToDto)
                .toList();
    }


}

