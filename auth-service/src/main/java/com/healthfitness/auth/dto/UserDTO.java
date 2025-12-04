package com.healthfitness.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
