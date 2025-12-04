package com.healthfitness.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class JwtResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private UUID id;
    private String email;
    private List<String> roles;
}
