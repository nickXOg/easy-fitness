package com.healthfitness.cardio.mapper;

import com.healthfitness.cardio.model.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.model.entity.CardioWorkout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardioWorkoutMapper {

    @Mapping(source = "workoutType.id", target = "workoutTypeId")
    CardioWorkoutDTO cardioWorkoutToDto(CardioWorkout entity);

    @Mapping(target = "workoutType.id", source = "workoutTypeId")
    CardioWorkout cardioWorkoutToEntity(CardioWorkoutDTO dto);
}
