package com.healthfitness.cardio.mapper;

import com.healthfitness.cardio.dto.CardioWorkoutDTO;
import com.healthfitness.cardio.entity.CardioWorkout;
import com.healthfitness.cardio.entity.WorkoutType;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-18T16:08:45+0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class CardioWorkoutMapperImpl implements CardioWorkoutMapper {

    @Override
    public CardioWorkoutDTO cardioWorkoutToDto(CardioWorkout entity) {
        if ( entity == null ) {
            return null;
        }

        CardioWorkoutDTO cardioWorkoutDTO = new CardioWorkoutDTO();

        cardioWorkoutDTO.setWorkoutTypeId( entityWorkoutTypeId( entity ) );
        cardioWorkoutDTO.setId( entity.getId() );
        cardioWorkoutDTO.setUserId( entity.getUserId() );
        cardioWorkoutDTO.setDistance( entity.getDistance() );
        cardioWorkoutDTO.setDuration( entity.getDuration() );
        cardioWorkoutDTO.setAverageHeartRate( entity.getAverageHeartRate() );
        cardioWorkoutDTO.setMaxHeartRate( entity.getMaxHeartRate() );
        cardioWorkoutDTO.setCaloriesBurned( entity.getCaloriesBurned() );
        cardioWorkoutDTO.setStartTime( entity.getStartTime() );
        cardioWorkoutDTO.setEndTime( entity.getEndTime() );
        cardioWorkoutDTO.setLocation( entity.getLocation() );
        cardioWorkoutDTO.setNotes( entity.getNotes() );

        return cardioWorkoutDTO;
    }

    @Override
    public CardioWorkout cardioWorkoutToEntity(CardioWorkoutDTO dto) {
        if ( dto == null ) {
            return null;
        }

        CardioWorkout cardioWorkout = new CardioWorkout();

        cardioWorkout.setId( dto.getId() );
        cardioWorkout.setUserId( dto.getUserId() );
        cardioWorkout.setDistance( dto.getDistance() );
        cardioWorkout.setDuration( dto.getDuration() );
        cardioWorkout.setAverageHeartRate( dto.getAverageHeartRate() );
        cardioWorkout.setMaxHeartRate( dto.getMaxHeartRate() );
        cardioWorkout.setCaloriesBurned( dto.getCaloriesBurned() );
        cardioWorkout.setStartTime( dto.getStartTime() );
        cardioWorkout.setEndTime( dto.getEndTime() );
        cardioWorkout.setLocation( dto.getLocation() );
        cardioWorkout.setNotes( dto.getNotes() );

        return cardioWorkout;
    }

    private UUID entityWorkoutTypeId(CardioWorkout cardioWorkout) {
        if ( cardioWorkout == null ) {
            return null;
        }
        WorkoutType workoutType = cardioWorkout.getWorkoutType();
        if ( workoutType == null ) {
            return null;
        }
        UUID id = workoutType.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
