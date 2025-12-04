package com.healthfitness.cardio.mapper;

import com.healthfitness.cardio.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.entity.CardioWorkout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardioWorkoutMapper {

    @Mapping(source = "workoutType.id", target = "workoutTypeId")
    CardioWorkoutDTO cardioWorkoutToDto(CardioWorkout entity);

    CardioWorkout cardioWorkoutToEntity(CardioWorkoutDTO dto);
}
