package com.healthfitness.cardio.repository;

import com.healthfitness.cardio.entity.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkoutTypeRepository extends JpaRepository<WorkoutType, UUID> {
}
