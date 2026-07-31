package com.turf.turf_management.controller;

import com.turf.turf_management.dto.auth.AuthRequest;
import com.turf.turf_management.dto.auth.AuthResponse;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.repository.PlayerRepository;
import com.turf.turf_management.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PlayerRepository playerRepository; // ✅ ADD THIS

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }

        UserDetails user = userDetailsService.loadUserByUsername(request.getEmail());

        String role = user.getAuthorities().iterator().next().getAuthority();

        String token = jwtUtil.generateToken(user.getUsername(), role);

        // 🔥 FETCH PLAYER FROM DB
        Player player = playerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        return new AuthResponse(token, role, player.getPlayerId()); // ✅ SEND playerId

    }
}
