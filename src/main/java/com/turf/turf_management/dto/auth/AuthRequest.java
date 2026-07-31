package com.turf.turf_management.dto.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
    private String role;
}
