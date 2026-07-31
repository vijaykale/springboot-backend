package com.turf.turf_management.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtil {
    public static String getCurrentUser(Authentication auth) {
        return auth.getName();
    }

    public static boolean isAdmin(Authentication auth) {
        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMIN"));
    }
}
