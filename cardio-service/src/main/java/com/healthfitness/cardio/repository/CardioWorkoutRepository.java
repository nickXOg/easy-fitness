package com.healthfitness.cardio.repository;

import com.healthfitness.cardio.entity.CardioWorkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardioWorkoutRepository extends JpaRepository<CardioWorkout, UUID> {
    List<CardioWorkout> findByUserId(Long userId);

    // ⚡ Bolt: Added JOIN FETCH to solve N+1 query problem when fetching workouts by user ID.
    // This reduces the number of queries from 1+N to a single query.
    @Query("SELECT cw FROM CardioWorkout cw JOIN FETCH cw.workoutType WHERE cw.userId = :userId")
    List<CardioWorkout> findAllByUserIdWithWorkoutType(Long userId);
}
