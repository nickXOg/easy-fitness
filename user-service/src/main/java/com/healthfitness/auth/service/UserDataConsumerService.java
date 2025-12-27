package com.healthfitness.auth.service;

import com.healthfitness.auth.entity.User;
import com.healthfitness.auth.message.UserDataMessage;
import com.healthfitness.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataConsumerService {

    private final UserRepository userRepository;

    @KafkaListener(topics = "${kafka.topic.user-data:userDataTopic}", groupId = "user-data-group")
    public void consumeUserData(UserDataMessage userDataMessage) {
        log.info("Received user data from Kafka: {}", userDataMessage);

        try {
            // Check if user already exists by keycloakId
            Optional<User> existingUser = userRepository.findByKeycloakId(UUID.fromString(userDataMessage.getKeycloakId()));

            User user;
            if (existingUser.isPresent()) {
                // Update existing user
                user = existingUser.get();
                user.setEmail(userDataMessage.getEmail());
                user.setFirstName(userDataMessage.getFirstName());
                user.setLastName(userDataMessage.getLastName());
                user.setDateOfBirth(userDataMessage.getDateOfBirth());
                log.info("Updating existing user with ID: {}", user.getId());
            } else {
                // Create new user
                user = new User();
                user.setKeycloakId(UUID.fromString(userDataMessage.getKeycloakId()));
                user.setEmail(userDataMessage.getEmail());
                user.setFirstName(userDataMessage.getFirstName());
                user.setLastName(userDataMessage.getLastName());
                user.setDateOfBirth(userDataMessage.getDateOfBirth());
                log.info("Creating new user with keycloakId: {}", userDataMessage.getKeycloakId());
            }

            userRepository.save(user);
            log.info("Successfully saved user data for keycloakId: {}", userDataMessage.getKeycloakId());
        } catch (Exception e) {
            log.error("Error processing user data from Kafka", e);
            throw e;
        }
    }
}