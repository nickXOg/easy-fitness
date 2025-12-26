package com.healthfitness.auth.service;

import com.healthfitness.auth.message.UserDataMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDataPublisher {

    private final KafkaTemplate<String, UserDataMessage> kafkaTemplate;

    @Value("${kafka.topic.user-data:userDataTopic}")
    private String userDataTopic;

    public void publishUserData(UserDataMessage userData) {
        try {
            String keycloakId = userData.getKeycloakId();

            // Send to Kafka
            kafkaTemplate.send(userDataTopic, keycloakId, userData);
            log.info("Published user data for user {} to topic {}", keycloakId, userDataTopic);
        } catch (Exception e) {
            log.error("Error publishing user data to Kafka", e);
            throw e;
        }
    }
}