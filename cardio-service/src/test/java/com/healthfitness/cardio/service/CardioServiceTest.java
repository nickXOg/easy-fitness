package com.healthfitness.cardio.service;

import com.healthfitness.cardio.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.entity.CardioWorkout;
import com.healthfitness.cardio.entity.WorkoutType;
import com.healthfitness.cardio.mapper.CardioWorkoutMapper;
import com.healthfitness.cardio.repository.CardioWorkoutRepository;
import com.healthfitness.cardio.repository.WorkoutTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardioServiceTest {

    @Mock
    private CardioWorkoutRepository cardioWorkoutRepository;

    @Mock
    private WorkoutTypeRepository workoutTypeRepository;

    @Mock
    private CardioEventPublisher cardioEventPublisher;

    @Mock
    private CardioWorkoutMapper mapper;

    @InjectMocks
    private CardioService cardioService;

    @Test
    void shouldCreateWorkoutSuccessfully() {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        UUID workoutId = UUID.randomUUID();
        
        CardioWorkoutDTO inputDto = new CardioWorkoutDTO();
        inputDto.setUserId(1L);
        inputDto.setWorkoutTypeId(workoutTypeId);
        inputDto.setDistance(5.0);
        inputDto.setDuration(30);
        inputDto.setCaloriesBurned(300);
        inputDto.setStartTime(LocalDateTime.now());
        inputDto.setEndTime(LocalDateTime.now().plusMinutes(30));

        WorkoutType workoutType = new WorkoutType();
        workoutType.setId(workoutTypeId);
        workoutType.setName("Running");

        CardioWorkout entityToSave = new CardioWorkout();
        entityToSave.setUserId(1L);
        entityToSave.setWorkoutType(workoutType);
        entityToSave.setDistance(5.0);
        entityToSave.setDuration(30);
        entityToSave.setCaloriesBurned(300);
        entityToSave.setStartTime(inputDto.getStartTime());
        entityToSave.setEndTime(inputDto.getEndTime());

        CardioWorkout savedEntity = new CardioWorkout();
        savedEntity.setId(workoutId);
        savedEntity.setUserId(1L);
        savedEntity.setWorkoutType(workoutType);
        savedEntity.setDistance(5.0);
        savedEntity.setDuration(30);
        savedEntity.setCaloriesBurned(300);
        savedEntity.setStartTime(inputDto.getStartTime());
        savedEntity.setEndTime(inputDto.getEndTime());

        CardioWorkoutDTO outputDto = new CardioWorkoutDTO();
        outputDto.setId(workoutId);
        outputDto.setUserId(1L);
        outputDto.setWorkoutTypeId(workoutTypeId);
        outputDto.setDistance(5.0);
        outputDto.setDuration(30);
        outputDto.setCaloriesBurned(300);
        outputDto.setStartTime(inputDto.getStartTime());
        outputDto.setEndTime(inputDto.getEndTime());

        when(workoutTypeRepository.findById(workoutTypeId)).thenReturn(Optional.of(workoutType));
        when(cardioWorkoutRepository.save(any(CardioWorkout.class))).thenReturn(savedEntity);
        when(mapper.cardioWorkoutToDto(any(CardioWorkout.class))).thenReturn(outputDto);

        // When
        CardioWorkoutDTO result = cardioService.createWorkout(inputDto);

        // Then
        assertNotNull(result);
        assertEquals(workoutId, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(5.0, result.getDistance());
        assertEquals(30, result.getDuration());
        assertEquals(300, result.getCaloriesBurned());

        verify(cardioWorkoutRepository).save(any(CardioWorkout.class));
        verify(cardioEventPublisher).publishWorkoutCreated(any(CardioWorkout.class));
        verify(mapper).cardioWorkoutToDto(any(CardioWorkout.class));
    }

    @Test
    void shouldThrowExceptionWhenWorkoutTypeNotFound() {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        
        CardioWorkoutDTO inputDto = new CardioWorkoutDTO();
        inputDto.setUserId(1L);
        inputDto.setWorkoutTypeId(workoutTypeId);
        inputDto.setDistance(5.0);
        inputDto.setDuration(30);
        inputDto.setCaloriesBurned(300);

        when(workoutTypeRepository.findById(workoutTypeId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardioService.createWorkout(inputDto);
        });

        assertEquals("Workout type not found", exception.getMessage());
        verify(cardioWorkoutRepository, never()).save(any(CardioWorkout.class));
    }

    @Test
    void shouldGetWorkoutsByUserIdSuccessfully() {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        UUID workoutId1 = UUID.randomUUID();
        UUID workoutId2 = UUID.randomUUID();
        
        CardioWorkout entity1 = new CardioWorkout();
        entity1.setId(workoutId1);
        entity1.setUserId(1L);
        entity1.setWorkoutType(new WorkoutType());
        entity1.getWorkoutType().setId(workoutTypeId);
        entity1.setDistance(5.0);
        entity1.setDuration(30);
        entity1.setCaloriesBurned(300);
        entity1.setStartTime(LocalDateTime.now());

        CardioWorkout entity2 = new CardioWorkout();
        entity2.setId(workoutId2);
        entity2.setUserId(1L);
        entity2.setWorkoutType(new WorkoutType());
        entity2.getWorkoutType().setId(workoutTypeId);
        entity2.setDistance(10.0);
        entity2.setDuration(60);
        entity2.setCaloriesBurned(600);
        entity2.setStartTime(LocalDateTime.now().plusDays(1));

        List<CardioWorkout> entities = Arrays.asList(entity1, entity2);
        when(cardioWorkoutRepository.findAllByUserIdWithWorkoutType(1L)).thenReturn(entities);

        CardioWorkoutDTO dto1 = new CardioWorkoutDTO();
        dto1.setId(workoutId1);
        dto1.setUserId(1L);
        dto1.setWorkoutTypeId(workoutTypeId);
        dto1.setDistance(5.0);
        dto1.setDuration(30);
        dto1.setCaloriesBurned(300);
        dto1.setStartTime(entity1.getStartTime());

        CardioWorkoutDTO dto2 = new CardioWorkoutDTO();
        dto2.setId(workoutId2);
        dto2.setUserId(1L);
        dto2.setWorkoutTypeId(workoutTypeId);
        dto2.setDistance(10.0);
        dto2.setDuration(60);
        dto2.setCaloriesBurned(600);
        dto2.setStartTime(entity2.getStartTime());

        List<CardioWorkoutDTO> dtos = Arrays.asList(dto1, dto2);
        when(mapper.cardioWorkoutToDto(entity1)).thenReturn(dto1);
        when(mapper.cardioWorkoutToDto(entity2)).thenReturn(dto2);

        // When
        List<CardioWorkoutDTO> result = cardioService.getWorkoutsByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(workoutId1, result.get(0).getId());
        assertEquals(workoutId2, result.get(1).getId());

        verify(cardioWorkoutRepository).findAllByUserIdWithWorkoutType(1L);
        verify(mapper).cardioWorkoutToDto(entity1);
        verify(mapper).cardioWorkoutToDto(entity2);
    }
}