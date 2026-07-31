package com.turf.turf_management.controller;

import com.turf.turf_management.dto.player.PlayerRequestDTO;
import com.turf.turf_management.dto.player.PlayerResponseDTO;
import com.turf.turf_management.dto.player.PlayerUpdateRequestDTO;
import com.turf.turf_management.model.Player;
import com.turf.turf_management.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/players")
@RequiredArgsConstructor
@Slf4j
public class PlayerController {

    private final PlayerService playerService;

    private Player birthdayService;

    @PostMapping
    public PlayerResponseDTO createPlayer(@Valid @RequestBody PlayerRequestDTO request) {
        log.info("API called: create player");
        return playerService.createPlayer(request);
    }

    @GetMapping
    public List<PlayerResponseDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{playerId}")
    public PlayerResponseDTO getPlayer(@PathVariable String playerId) {
        return playerService.getPlayerById(playerId);
    }

    @PutMapping("/{playerId}")
    public PlayerResponseDTO updatePlayer(
            @PathVariable String playerId,
            @Valid @RequestBody PlayerUpdateRequestDTO requestDTO) {
        return playerService.updatePlayer(playerId, requestDTO);
    }


    @PutMapping("/{playerId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable String playerId,
            @RequestParam boolean active) {

        playerService.updatePlayerStatus(playerId, active);
        return ResponseEntity.ok("Player status updated successfully");
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable String playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.ok("Player with ID " + playerId + " deactivated successfully");
    }

    @GetMapping("/birthdays")
    public List<Player> getBirthdays() {
        return playerService.getBirthdayPlayersOfMonth();
    }

    @PostMapping("/birthdays/send-wishes/{playerId}")
    public String sendBirthdayWish(@PathVariable String playerId) {
        return playerService.sendBirthdayWishes(playerId);
    }

}
