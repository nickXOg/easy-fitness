package com.healthfitness.auth.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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