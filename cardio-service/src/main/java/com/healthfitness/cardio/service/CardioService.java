package com.healthfitness.cardio.service;

import com.healthfitness.cardio.kafka.CardioEventPublisher;
import com.healthfitness.cardio.mapper.CardioWorkoutMapper;
import com.healthfitness.cardio.model.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.model.entity.CardioWorkout;
import com.healthfitness.cardio.repository.CardioWorkoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardioService {
    private final CardioWorkoutRepository cardioWorkoutRepository;
    private final CardioEventPublisher cardioEventPublisher;
    private final CardioWorkoutMapper mapper;

    public CardioWorkoutDTO createWorkout(CardioWorkoutDTO cardioWorkoutDTO) {
        CardioWorkout savedWorkout = cardioWorkoutRepository.save(mapper.cardioWorkoutToEntity(cardioWorkoutDTO));
        cardioEventPublisher.publishWorkoutCreated(savedWorkout);
        return mapper.cardioWorkoutToDto(savedWorkout);
    }

    public List<CardioWorkoutDTO> getWorkoutsByUserId(Long userId) {
        return cardioWorkoutRepository.findAllByUserIdWithWorkoutType(userId).stream()
                .map(mapper::cardioWorkoutToDto)
                .toList();
    }
}
