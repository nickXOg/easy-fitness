package com.healthfitness.cardio.mapper;

import com.healthfitness.cardio.model.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.model.entity.CardioWorkout;
import com.healthfitness.cardio.model.entity.WorkoutType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
class CardioWorkoutMapperTest {

    @Autowired
    private CardioWorkoutMapper mapper;

    @Test
    void shouldMapEntityToDto() {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        UUID workoutId = UUID.randomUUID();
        
        CardioWorkout entity = new CardioWorkout();
        entity.setId(workoutId);
        entity.setUserId(1L);
        
        WorkoutType workoutType = new WorkoutType();
        workoutType.setId(workoutTypeId);
        entity.setWorkoutType(workoutType);
        
        entity.setDistance(5.0);
        entity.setDuration(30);
        entity.setCaloriesBurned(300);
        entity.setStartTime(LocalDateTime.now());
        entity.setEndTime(LocalDateTime.now().plusMinutes(30));
        entity.setLocation("Park");
        entity.setNotes("Morning run");

        // When
        CardioWorkoutDTO dto = mapper.cardioWorkoutToDto(entity);

        // Then
        assertNotNull(dto);
        assertEquals(workoutId, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals(workoutTypeId, dto.getWorkoutTypeId());
        assertEquals(5.0, dto.getDistance());
        assertEquals(30, dto.getDuration());
        assertEquals(300, dto.getCaloriesBurned());
        assertEquals("Park", dto.getLocation());
        assertEquals("Morning run", dto.getNotes());
    }

    @Test
    void shouldMapDtoToEntity() {
        // Given
        UUID workoutTypeId = UUID.randomUUID();
        UUID workoutId = UUID.randomUUID();
        
        CardioWorkoutDTO dto = new CardioWorkoutDTO();
        dto.setId(workoutId);
        dto.setUserId(1L);
        dto.setWorkoutTypeId(workoutTypeId);
        dto.setDistance(5.0);
        dto.setDuration(30);
        dto.setCaloriesBurned(300);
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusMinutes(30));
        dto.setLocation("Gym");
        dto.setNotes("Cardio session");

        // When
        CardioWorkout entity = mapper.cardioWorkoutToEntity(dto);

        // Then
        assertNotNull(entity);
        assertEquals(workoutId, entity.getId());
        assertEquals(1L, entity.getUserId());
        assertEquals(5.0, entity.getDistance());
        assertEquals(30, entity.getDuration());
        assertEquals(300, entity.getCaloriesBurned());
        assertEquals("Gym", entity.getLocation());
        assertEquals("Cardio session", entity.getNotes());
    }
}