package com.healthfitness.gateway.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDataMessage {
    private String keycloakId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
}