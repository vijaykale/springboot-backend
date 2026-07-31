package com.turf.turf_management.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String role;
    private String playerId; // ✅ ADD THIS

    public AuthResponse(String token, String role, String playerId) {
        this.token = token;
        this.role = role;
        this.playerId = playerId;

    }
}
